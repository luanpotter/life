package xyz.luan.life.model;

import javafx.geometry.Point2D;
import javafx.scene.paint.*;
import javafx.scene.shape.Polygon;

/**
 * Created by lucas-cleto on 10/23/15.
 */
public class EntityShape extends Polygon {

    public static final int PRECISION = 100;
    public static final double ARC = 2 * Math.PI / (double) PRECISION;

    private Point2D center;
    private double[] characteristics;
    private Color color;
    private Point2D[] points;

    public EntityShape(Point2D center, double[] characteristics, Color color) {
        this.center = center;
        this.characteristics = characteristics;
        this.color = color;

        generatePoints();
        this.setFill(color);
    }

    private Point2D getPoint(double t) {
        int i = 0;
        double a = characteristics[i++] * Math.pow(Math.sin(characteristics[i++] * t), 2);
        double b = characteristics[i++] * Math.pow(Math.sin(characteristics[i++] * t), 2) * Math.pow(Math.cos(characteristics[i++] * t), 2);
        double c = characteristics[i++] * Math.pow(Math.cos(characteristics[i++] * t), 2);
        double d = characteristics[i++] * Math.sin(characteristics[i++] * t);
        double e = characteristics[i++] * Math.sin(characteristics[i++] * t) * Math.cos(characteristics[i++] * t);
        double f = characteristics[i++] * Math.cos(characteristics[i++] * t);
        double radius =  4 * (a + b + c + d + e + f);
        return new Point2D(center.getX() + radius * Math.cos(t), center.getY() + radius * Math.sin(t));
    }

    private void generatePoints() {
        points = new Point2D[PRECISION];
        for (int i = 0; i < PRECISION; i++) {
            Point2D point = getPoint(i * ARC);
            points[i] = point;
            this.getPoints().addAll(point.getX(), point.getY());
        }
    }

    public double estimateArea() {
        double sum = 0;
        for (int i = 0; i < (PRECISION - 1); i++) {
            sum += points[i].getX() * points[i + 1].getY() + points[i].getY() * points[i + 1].getX();
        }
        sum += points[PRECISION - 1].getX() * points[0].getY() + points[PRECISION - 1].getY() * points[0].getX();

        return (double) Math.abs(sum) / 2d;
    }

    public Point2D getCenter() {
        return center;
    }

    public void setCenter(Point2D center) {
        this.center = center;
    }

    public double[] getCharacteristics() {
        return characteristics;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point2D[] getPoints2D() {
        return points;
    }
}
