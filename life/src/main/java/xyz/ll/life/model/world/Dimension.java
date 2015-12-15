package xyz.ll.life.model.world;

import xyz.luan.geometry.Point;

public class Dimension extends Point {

    public Dimension(double w, double h) {
        super(w, h);
    }

    public double getWidth() {
        return this.getX();
    }

    public double getHeight() {
        return this.getY();
    }

    public double area() {
        return getWidth() * getHeight();
    }
}
