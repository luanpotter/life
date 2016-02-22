package xyz.ll.life.pca;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
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
import org.jzy3d.analysis.AnalysisLauncher;
import xyz.ll.life.model.Individual;
import xyz.luan.geometry.Point;

import java.util.*;

/**
 * Created by lucas-cleto on 2/16/16.
 */
public class PCA {

    private HashMap<UUID, SimplifiedIndividual> simplifiedIndividuals;
    private List<Snapshot> snapshots;
    private SparkContext sparkContext;

    public PCA() {
        SparkConf conf = new SparkConf().setAppName("PCA Example")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local");
        this.sparkContext = new SparkContext(conf);
        this.snapshots = new ArrayList<>();
        this.simplifiedIndividuals = new HashMap<>();
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

        for (int i = 0; i < individuals.size(); i++) {
            Individual individual = individuals.get(i);
            UUID uuid = individual.getUUID();
            UUID[] parents = individual.getParents();
            double[] genome = genetics[i];
            double[] principalComponents = pca[i];

            Point point = new Point(pca[i][0], pca[i][1]);
            int species = 0; // TODO

            SimplifiedIndividual simplifiedIndividual = new SimplifiedIndividual(uuid, parents, genome, principalComponents, species);
            if (!simplifiedIndividuals.containsKey(uuid)) {
                simplifiedIndividuals.put(uuid, simplifiedIndividual);
            }

            snapshot.add(new PCAPoint(uuid, point, species));
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

    private PhylogeneticsTree generatePhylogeneticsTree(int begin, int end, int step) {
        Map<PCAPoint, Integer> pointsIndexes = new HashMap<>();
        ArrayList<Point3D> points = new ArrayList<>();
        ArrayList<Integer> colors = new ArrayList<>();
        ArrayList<Point2D> lines = new ArrayList<>();

        Snapshot current = null, previous;
        for (int i = begin, z = 0; i < end; i += step, z++) {
            previous = current;
            current = snapshots.get(i);

            for (PCAPoint p : current.getPoints()) {
                int currentIndex = colors.size();
                colors.add(p.getSpecies());
                points.add(new Point3D(p.getPoint().getX(), p.getPoint().getY(), z));
                if (previous != null) {
                    PCAPoint previousPoint = previous.findPoint(p.getIndividualId());
                    if (previousPoint != null) {
                        lines.add(new Point2D(pointsIndexes.get(previousPoint), currentIndex));
                    }
                }
                pointsIndexes.put(p, currentIndex);
            }
        }

        Point3D[] p = points.toArray(new Point3D[points.size()]);
        Integer[] c = colors.toArray(new Integer[colors.size()]);
        Point2D[] l = lines.toArray(new Point2D[lines.size()]);
        PhylogeneticsTree phylogeneticsTree = new PhylogeneticsTree(p, c, l);
        return phylogeneticsTree;
    }

    public void showPhylogeneticsTreeAnalyser(int begin, int end, int step) {
        PhylogeneticsTreeAnalyser analyser = new PhylogeneticsTreeAnalyser(generatePhylogeneticsTree(begin, end, step));

        try {
            AnalysisLauncher.open(analyser);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showPhylogeneticsTreeAnalyser() {
        showPhylogeneticsTreeAnalyser(0, snapshots.size(), 1);
    }
}
