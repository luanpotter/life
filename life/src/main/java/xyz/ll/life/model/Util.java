package xyz.ll.life.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;

public class Util {

	public static final double INFINITY = Double.POSITIVE_INFINITY;

	public static final Color FOOD_COLOR = Color.GRAY;

    public static final double BASE_LIFE_ENERGY_COST = 0.2d;
	public static final double BASE_METABOLIZATION_ENERGY_COST = 0.1d;
	public static final double BASE_ENERGY_RELEASED = 0.9d;
	public static final double BASE_STRUCTURE_ENERGY = 0.5d;



	public static Point2D rotate(Point2D p, double theta) {
		return new Rotate(Math.toDegrees(theta), 0, 0).transform(p);
	}
}
