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

    public void iterate(List<Individual> individuals) {
        double[][] genetics = new double[individuals.size()][];
        int i = 0;
        for (Individual individual : individuals) {
            genetics[i] = individual.getGenome().getValue();
            i++;
        }

        long time = System.currentTimeMillis();

        double[][] covariance = covariance(genetics);
        double[][] pca = pca(covariance);
    }
}
