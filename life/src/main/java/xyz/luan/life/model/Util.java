package xyz.luan.life.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Util {

    public static final double INFINITY = Double.POSITIVE_INFINITY;

    public static final double DEFAULT_INDIVIDUAL_COLOR_HUE = Math.PI;
    public static final double DEFAULT_INDIVIDUAL_CHARITY = 0d;
    public static final double DEFAULT_INDIVIDUAL_LIBIDO = 0d;
    public static final double DEFAULT_INDIVIDUAL_MORFOLOGY = 1d;
    public static final double DEFAULT_INDIVIDUAL_COLOR_SATURATION = 1;
    public static final double DEFAULT_INDIVIDUAL_COLOR_VALUE = 1;

    public static final Color DEFAULT_FOOD_COLOR = Color.GRAY;

    public static final double BASE_LIFE_ENERGY_COST = 0.2d;
    public static final double BASE_REPRODUCTION_ENERGY_COST = 1000d;
    public static final double BASE_METABOLIZATION_ENERGY_COST = 0.1d;
    public static final double BASE_ENERGY_RELEASED = 0.9d;
    public static final double BASE_STRUCTURE_ENERGY = 0.5d;

    public static final double ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE = 10d;
    public static final double ACCEPTABLE_AREA_PROPORTION_TO_EAT = 1.5d;
    public static final int RARITY_OF_IMMUTABILITY = 10;

    public static Point2D rotate(Point2D p, double thetaDegrees) {
        return new Rotate(thetaDegrees, 0, 0).transform(p);
    }
}
