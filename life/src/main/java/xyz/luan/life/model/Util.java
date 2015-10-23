package xyz.luan.life.model;

import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

import java.util.Map.Entry;

public class Util {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    public static final Color[] COLORS = {Color.RED, Color.GREEN, Color.BLUE};

    public static final Color DEFAULT_INDIVIDUAL_COLOR = Color.WHITE;
    public static final double DEFAULT_INDIVIDUAL_CHARITY = 0;
    public static final double DEFAULT_INDIVIDUAL_LIBIDO = 0;
    public static final double DEFAULT_INDIVIDUAL_SIZE = 1;

    public static final double BASE_LIFE_ENERGY_COST = 0.2;
    public static final double BASE_REPRODUCTION_ENERGY_COST = 2;
    public static final double BASE_METABOLIZATION_ENERGY_COST = 0.1;
    public static final double BASE_ENERGY_RELEASED = 0.9;
    public static final double BASE_STRUCTURE_ENERGY = 0.5;

    public static final double ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE = 2;
    public static final double ACCEPTABLE_AREA_PROPORTION_TO_EAT = 2;
    public static final int RARITY_OF_IMMUTABILITY = 10;

    public static double getArea(Polygon polygon) {
        ObservableList<Double> points = polygon.getPoints();
        int n = points.size() / 2;
        double[] x = new double[n];
        double[] y = new double[n];
        for (int i = 0, j = 0; i < points.size(); i += 2, j++) {
            x[j] = points.get(i);
            y[j] = points.get(i + 1);
        }

        double sum = 0;
        for (int i = 0; i < (n - 1); i++) {
            sum += x[i] * y[i+1] + y[i] * x[i+1];
        }
        sum += x[n - 1] * y[0] + y[n - 1] * x[0];

        return (double) Math.abs(sum) / 2d;
    }
}
