package xyz.ll.life.pca;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class PhylogeneticsTree {

    private Point3D[] points;
    private int[] colors;
    private Point2D[] connections;

    public PhylogeneticsTree(Point3D[] points, int[] colors, Point2D[] connections) {
        this.points = points;
        this.colors = colors;
        this.connections = connections;
    }

    public Point3D[] getPoints() {
        return points;
    }

    public void setPoints(Point3D[] points) {
        this.points = points;
    }

    public int[] getColors() {
        return colors;
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public Point2D[] getConnections() {
        return connections;
    }

    public void setConnections(Point2D[] connections) {
        this.connections = connections;
    }
}
