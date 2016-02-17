package xyz.ll.life.pca;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import xyz.ll.life.model.Individual;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lucas-cleto on 2/16/16.
 */
public class PCA {

    private List<Snapshot> snapshots;
    private SparkContext sparkContext;

    public PCA() {
        SparkConf conf = new SparkConf().setAppName("PCA Example")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local");
        this.sparkContext = new SparkContext(conf);
        this.snapshots = new LinkedList<>();
    }

    private double[][] covariance(double[][] genetics) {
        RealMatrix geneticMatrix = MatrixUtils.createRealMatrix(genetics);
        geneticMatrix = geneticMatrix.transpose();
        return new Covariance(geneticMatrix).getCovarianceMatrix().getData();
    }

    private double[][] pca(double[][] covariance) {
        LinkedList<Vector> rowsList = new LinkedList<Vector>();
        for (int i = 0; i < covariance.length; i++) {
            Vector currentRow = Vectors.dense(covariance[i]);
            rowsList.add(currentRow);
        }
        JavaRDD<Vector> rows = JavaSparkContext.fromSparkContext(sparkContext).parallelize(rowsList);
        RowMatrix mat = new RowMatrix(rows.rdd());
        Matrix pc = mat.computePrincipalComponents(2);
        RowMatrix projected = mat.multiply(pc);

        double[][] pca = new double[covariance.length][];
        Vector[] projectedRows = (Vector[]) projected.rows().collect();
        int i = 0;
        for (Vector row : projectedRows) {
            pca[i] = row.toArray();
            i++;
        }
        return pca;
    }

    private double[][] extractGenetics(List<Individual> individuals) {
        double[][] genetics = new double[individuals.size()][];
        int i = 0;
        for (Individual individual : individuals) {
            genetics[i] = individual.getGenome().getValue();
            i++;
        }
        return genetics;
    }

    private Snapshot generateSnapshot(List<Individual> individuals, double[][] genetics, double[][] pca, long time) {
        Snapshot snapshot = new Snapshot(time);
        int i = 0;
        for (Individual individual : individuals) {
            UUID uuid = individual.getUUID();
            UUID[] parents = individual.getParents();
            double[] genome = genetics[i];
            double[] principalComponents = pca[i];
            int specie = 0;
            SimplifiedIndividual simplifiedIndividual = new SimplifiedIndividual(
                    uuid, parents, genome, principalComponents, specie);
            snapshot.add(uuid, simplifiedIndividual);
        }
        return snapshot;
    }

    public void iterate(List<Individual> individuals) {
        double[][] genetics = extractGenetics(individuals);

        long time = System.currentTimeMillis();

        double[][] covariance = covariance(genetics);
        double[][] pca = pca(covariance);

        Snapshot snapshot = generateSnapshot(individuals, genetics, pca, time);

        this.snapshots.add(snapshot);
    }
}
