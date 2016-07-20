package xyz.ll.life.pca;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

import java.util.Arrays;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class PhylogeneticsTree {

    private Point3D[] points;
    private Integer[] colors;
    private Point2D[] connections;

    public PhylogeneticsTree(Point3D[] points, Integer[] colors, Point2D[] connections) {
        this.points = points;
        this.colors = colors;
        this.connections = connections;
    }

    public int countColors() {
        int maxColor = -1;
        for (int color : colors) {
            if (color > maxColor) {
                maxColor = color;
            }
        }
        return maxColor;
    }

    public Point3D[] getPoints() {
        return points;
    }

    public void setPoints(Point3D[] points) {
        this.points = points;
    }

    public Integer[] getColors() {
        return colors;
    }

    public void setColors(Integer[] colors) {
        this.colors = colors;
    }

    public Point2D[] getConnections() {
        return connections;
    }

    public void setConnections(Point2D[] connections) {
        this.connections = connections;
    }

    @Override
    public String toString() {
        return Arrays.toString(points);
    }
}