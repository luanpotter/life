package xyz.ll.life.model.world;

import xyz.luan.geometry.Point;

public class Viewport {

    private Dimension dimension;
    private Point position;

    public Viewport(Dimension dimension) {
        this.dimension = dimension;
        this.position = new Point();
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public double getX() {
        return position.getX();
    }

    public double getY() {
        return position.getY();
    }

    public void newWidth(double width) {
        dimension.setX(width);
    }

    public void newHeight(double height) {
        dimension.setY(height);
    }
}
