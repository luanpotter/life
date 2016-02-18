package xyz.ll.life.lab;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.correlation.Covariance;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Matrix;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;
import org.apache.spark.mllib.linalg.distributed.RowMatrix;
import org.jzy3d.analysis.AnalysisLauncher;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.*;

import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Random;


/**
 * Created by lucas-cleto on 1/16/16.
 */
public class DiscreteSimulation {

    private static final Logger LOGGER = Logger.getLogger(DiscreteSimulation.class);

    private static final int[] DEL_X = {-1,  0,  1, -1,  0,  1, -1,  0,  1};
    private static final int[] DEL_Y = {-1, -1, -1,  0,  0,  0,  1,  1,  1};

    private static final int IMAGE_SIZE = 2000;
    private static final int ZOOM = 100;
    private static final int POINT_SIZE = 10;

    private static final int REPRODUCTION_SPACIAL_DISTANCE = 3;
    private static final int REPRODUCTION_GENIC_DISTANCE = 15;

    private static final int GENES = 100;
    private static final int MAX_FILE_NUMBER = 1;
    private static final int GENERATIONS_PER_FILE = 1000;
    private static final int FRAME_STEP = 100;
    private static final int POPULATION_SIZE = 1000;

    private static void plot(int[][] map, double[][] individuals) {
        System.out.println();
        System.out.println();

        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != -1) {
                    System.out.printf("%03d ", map[i][j]);
                } else {
                    System.out.print("... ");
                }
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();

        int part = individuals.length / 4;
        for (int i = 0; i < part; i++) {
            for (int k = 0; k < 4; k++) {
                System.out.printf("(%03d) ", k * part + i);
                for (int j = 0; j < individuals[i].length; j++) {
                    System.out.print(individuals[k * part + i][j] + " ");
                }
                System.out.print("    ");
            }
            System.out.println();
        }

        System.out.println();
        System.out.println();
    }

    private static boolean compatible(double[][] individuals, int a, int b) {
        int sum = 0;
        for (int i = 0; i < individuals[a].length; i++) {
            if (individuals[a][i] != individuals[b][i]) {
                sum += Math.abs(individuals[a][i] - individuals[b][i]);
            }
        }
        return sum <= REPRODUCTION_GENIC_DISTANCE;
    }

    private static int[][] populate(int[][] map, int[][] pos, Random random) {
        for (int i = 0; i < map.length; i++) {
            for (int j = 0; j < map[i].length; j++) {
                map[i][j] = -1;
            }
        }

        for (int i = 0; i < map.length; i++) {
            for (int j = -REPRODUCTION_SPACIAL_DISTANCE / 2; j < 1 + REPRODUCTION_SPACIAL_DISTANCE / 2; j++) {
                map[i][map.length / 2 + j] = -2;
            }
        }
        for (int i = 0; i < map[0].length; i++) {
            for (int j = -REPRODUCTION_SPACIAL_DISTANCE / 2; j < 1 + REPRODUCTION_SPACIAL_DISTANCE / 2; j++) {
                map[map[0].length / 2 + j][i] = -2;
            }
        }

        for (int k = 0; k < 4; k++) {
            for (int i = k * pos.length / 4; i < (k + 1) * pos.length / 4; i++) {
                while (true) {
                    int x = random.nextInt(map.length / 3) + ((k == 0 || k == 2) ? 0 : (2 * map.length / 3));
                    int y = random.nextInt(map[x].length / 3) + ((k == 0 || k == 1) ? 0 : (2 * map[x].length / 3));
                    if (map[x][y] == -1) {
                        map[x][y] = i;
                        pos[i][0] = x;
                        pos[i][1] = y;
                        break;
                    }
                }
            }
        }

        return map;
    }

    private static BufferedImage pca(double[][] individual, SparkContext sc, int time,
                                     Coord3d[] coords, org.jzy3d.colors.Color[] coordsColors, int[] coordsCont, int z,
                                     LineStrip[] lineStrips) {
        long aux = System.currentTimeMillis();
        Color[] colors = species(individual);
        System.out.println("Generation: " + time);
        System.out.println("Species time: " + (System.currentTimeMillis() - aux));

        aux = System.currentTimeMillis();

        //Transpose
        double[][] array = new double[individual[0].length][individual.length];
        for (int i = 0; i < individual.length; i++) {
            for (int j = 0; j < individual[i].length; j++) {
                array[j][i] = individual[i][j];
            }
        }
        RealMatrix mx = MatrixUtils.createRealMatrix(array);
        //mx = mx.transpose();
        RealMatrix cov = new Covariance(mx).getCovarianceMatrix();
        array = cov.getData();
        System.out.println("Covariance time: " + (System.currentTimeMillis() - aux));

        aux = System.currentTimeMillis();
        LinkedList<Vector> rowsList = new LinkedList<Vector>();
        for (int i = 0; i < array.length; i++) {
            Vector currentRow = Vectors.dense(array[i]);
            rowsList.add(currentRow);
        }
        JavaRDD<Vector> rows = JavaSparkContext.fromSparkContext(sc).parallelize(rowsList);
        RowMatrix mat = new RowMatrix(rows.rdd());
        Matrix pc = mat.computePrincipalComponents(2);
        RowMatrix projected = mat.multiply(pc);
        System.out.println("PCA time: " + (System.currentTimeMillis() - aux));

        aux = System.currentTimeMillis();
        BufferedImage image = new BufferedImage(IMAGE_SIZE, IMAGE_SIZE, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, IMAGE_SIZE, IMAGE_SIZE);
        g.setColor(Color.BLACK);
        g.drawString(time + "", 20, 20);

        Object newRows = projected.rows().collect();
        /*System.out.println("PCA " + ((Vector[]) newRows).length + "x2");*/
        int i = 0;
        for (Vector row : (Vector[]) newRows) {
            double[] vet =  row.toArray();
            /*for (double d : vet) {
                System.out.print(d + " ");
            }*/
            g.setColor(colors[i]);
            g.fillOval((int) (ZOOM * vet[0] + IMAGE_SIZE / 2), (int) (ZOOM * vet[1] + IMAGE_SIZE / 2), POINT_SIZE, POINT_SIZE);
            /*System.out.println();*/

            coords[coordsCont[0]] = new Coord3d(vet[0], vet[1], z);
            coordsColors[coordsCont[0]] = new org.jzy3d.colors.Color(
                    colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), colors[i].getAlpha());
            if (coordsCont[0] >= POPULATION_SIZE) {
                LineStrip ls = new LineStrip();
                ls.add(new org.jzy3d.plot3d.primitives.Point(
                        coords[coordsCont[0] - POPULATION_SIZE], coordsColors[coordsCont[0] - POPULATION_SIZE]));
                ls.add(new org.jzy3d.plot3d.primitives.Point(
                        coords[coordsCont[0]], coordsColors[coordsCont[0]]));
                ls.setDisplayed(true);
                lineStrips[coordsCont[0] - POPULATION_SIZE] = ls;
            }

            if (pcaDialog != null) {
                pcaDialog.addPoints(coords[coordsCont[0]]);
                pcaDialog.addColors(coordsColors[coordsCont[0]]);
                if (coordsCont[0] >= POPULATION_SIZE) {
                    pcaDialog.addLine(lineStrips[coordsCont[0] - POPULATION_SIZE]);
                }
            }

            coordsCont[0]++;

            i++;
        }
        /*System.out.println();*/
        System.out.println("Image time: " + (System.currentTimeMillis() - aux));
        System.out.println(".......................................");
        System.out.println();

        return image;
    }

    private static Color[] species(double[][] individual) {
        ArrayList<Integer> species = new ArrayList<>();
        int[] already = new int[individual.length];

        int contSpecies = 1;
        for (int i = 0; i < individual.length; i++) {
            if (already[i] == 0) {
                int cont = 0;
                Deque<Integer> queue = new ArrayDeque<>();
                queue.add(i);
                while (!queue.isEmpty()) {
                    int current = queue.pop();
                    if (already[current] == 0) {
                        already[current] = contSpecies;
                        cont++;
                        for (int j = 0; j < individual.length; j++) {
                            if (already[j] == 0 && compatible(individual, current, j)) {
                                queue.add(j);
                            }
                        }
                    }
                }
                species.add(cont);
                contSpecies++;
            }
        }

        Color[] colors = new Color[individual.length];
        for (int i = 0; i < colors.length; i++) {
            float hue = (float) (1f / (contSpecies - 1) * (already[i] - 1));
            colors[i] = Utils.color(hue);
        }

        System.out.println(species);

        return colors;
    }

    private static Pca3dDialog pcaDialog;

    public static void main(String[] args) throws Exception {
        SparkConf conf = new SparkConf().setAppName("PCA Example")
                .set("spark.driver.allowMultipleContexts", "true")
                .setMaster("local");
        SparkContext sc = new SparkContext(conf);

        Random random = new Random(0l);

        double[][] individuals = new double[POPULATION_SIZE][GENES];
        int[][] pos = new int[POPULATION_SIZE][2];
        int[][] map = new int[70][70];

        populate(map, pos, random);
        //plot(map, individuals);

        int t = 0;
        int z = 0;
        Coord3d[] coords = new Coord3d[POPULATION_SIZE * MAX_FILE_NUMBER * GENERATIONS_PER_FILE / FRAME_STEP];
        org.jzy3d.colors.Color[] colors = new org.jzy3d.colors.Color[coords.length];
        LineStrip[] lineStrips = new LineStrip[coords.length - POPULATION_SIZE];
        int[] coordsCont = {0};
        BufferedImage image = pca(individuals, sc, t, coords, colors, coordsCont, z++, lineStrips);

        Coord3d[] initCoord = {new Coord3d(0, 0, 0), new Coord3d(0, 0, 10),
                new Coord3d(1, 0, 0), new Coord3d(0, 1, 0), new Coord3d(-1, 0, 0), new Coord3d(0, -1, 0)};
        org.jzy3d.colors.Color[] initColor = {org.jzy3d.colors.Color.BLACK, org.jzy3d.colors.Color.BLACK,
                org.jzy3d.colors.Color.BLACK, org.jzy3d.colors.Color.BLACK, org.jzy3d.colors.Color.BLACK,
                org.jzy3d.colors.Color.BLACK};
        pcaDialog = new Pca3dDialog(initCoord, initColor, null);
        new Thread() {
            @Override
            public void run() {
                try {
                    AnalysisLauncher.open(pcaDialog);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (int fileNumber = 0; fileNumber < MAX_FILE_NUMBER; fileNumber++) {
            ImageOutputStream output =
                    new FileImageOutputStream(new File("pca" + fileNumber + ".gif"));
            GifSequenceWriter writer =
                    new GifSequenceWriter(output, BufferedImage.TYPE_INT_ARGB, 1, true);

            if (t == 0) {
                writer.writeToSequence(image);
                t++;
            }

            long aux = System.currentTimeMillis();
            for (; t < GENERATIONS_PER_FILE * (fileNumber + 1); t++) {
                individualsLoop:
                for (int i = 0; i < pos.length; i++) {
                    //scanning the surroundings
                    int startX = (pos[i][0] - REPRODUCTION_SPACIAL_DISTANCE) < 0 ? 0 : (pos[i][0] - REPRODUCTION_SPACIAL_DISTANCE);
                    int endX = (pos[i][0] + REPRODUCTION_SPACIAL_DISTANCE) > map.length ? map.length : (pos[i][0] + REPRODUCTION_SPACIAL_DISTANCE);
                    for (int x = startX; x < endX; x++) {
                        int startY = (pos[i][1] - REPRODUCTION_SPACIAL_DISTANCE) < 0 ? 0 : (pos[i][1] - REPRODUCTION_SPACIAL_DISTANCE);
                        int endY = (pos[i][1] + REPRODUCTION_SPACIAL_DISTANCE) > map[x].length ? map[x].length : (pos[i][1] + REPRODUCTION_SPACIAL_DISTANCE);
                        for (int y = startY; y < endY; y++) {
                            //looking for a pair
                            if (map[x][y] >= 0 && map[x][y] != i && compatible(individuals, i, map[x][y])) {
                                //get the pair
                                int pair = map[x][y];

                                //choose the ancestor for each half of dna
                                int begin, end;
                                if (random.nextBoolean()) {
                                    begin = 0;
                                    end = individuals[i].length / 2;
                                } else {
                                    begin = individuals[i].length / 2;
                                    end = individuals[i].length;
                                }

                                //merge the dna
                                for (int k = begin; k < end; k++) {
                                    individuals[i][k] = individuals[pair][k];
                                }

                                //potential mutation
                                for (int iGene = 0; iGene < individuals[i].length; iGene++) {
                                    if (random.nextInt(1000) == 0) {
                                        individuals[i][iGene] = (individuals[i][iGene] == 0) ? 1 : 0;
                                    }
                                }
                                /*if (random.nextInt(100) == 0) {
                                    int mutation = random.nextInt(individuals[i].length);
                                    individuals[i][mutation] = (individuals[i][mutation] == 0) ? 1 : 0;
                                    /*int mutation = random.nextInt(individuals[i].length);
                                    //individuals[i][mutation] = individuals[i][mutation] == 0 ? 1 : 0;
                                    double diff = random.nextDouble() / 10;
                                    if (random.nextBoolean()) {
                                        individuals[i][mutation] += diff;
                                    } else {
                                        individuals[i][mutation] -= diff;
                                    }
                                    if (individuals[i][mutation] > 1) {
                                        individuals[i][mutation] = 0;
                                    }
                                    if (individuals[i][mutation] < 0) {
                                        individuals[i][mutation] = 1;
                                    }
                                }*/

                                //move to the new position
                                if (random.nextInt(10) == 0) {
                                    int[] mov = new int[9];
                                    for (int j = 0; j < 9; j++) {
                                        mov[j] = j;
                                    }
                                    mov = Utils.shuffleArray(mov, random);
                                    for (int j = 0; j < 9; j++) {
                                        int xf = pos[i][0] + DEL_X[mov[j]];
                                        int yf = pos[i][1] + DEL_Y[mov[j]];

                                        if (xf < 0) {
                                            xf = 0;
                                        }
                                        if (xf > map.length - 1) {
                                            xf = map.length - 1;
                                        }
                                        if (yf < 0) {
                                            yf = 0;
                                        }
                                        if (yf > map[xf].length - 1) {
                                            yf = map[xf].length - 1;
                                        }

                                        if (map[xf][yf] == -1 || map[xf][yf] == i) {
                                            map[pos[i][0]][pos[i][1]] = -1;

                                            pos[i][0] = xf;
                                            pos[i][1] = yf;

                                            map[pos[i][0]][pos[i][1]] = i;
                                        }
                                    }
                                }

                                //continue
                                continue individualsLoop;
                            }
                        }
                    }
                }

                if (t % FRAME_STEP == 0) {
                    //plot(map, individuals);
                    try {
                        writer.writeToSequence(pca(individuals, sc, t, coords, colors, coordsCont, z++, lineStrips));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            System.out.println("Total time: " + (System.currentTimeMillis() - aux));
            System.out.println(".......................................");
            System.out.println();

            writer.close();
            output.close();
        }
    }
}
