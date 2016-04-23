package xyz.ll.life.model.world;

import javafx.scene.canvas.GraphicsContext;
import xyz.luan.geometry.Point;

public class Viewport {

    private Dimension dimension;
    private Point position;
    private double zoom;

    public Viewport(Dimension dimension) {
        this.dimension = dimension;
        this.position = new Point();
        this.zoom = 1d;
    }

    public double getWidth() {
        return dimension.getWidth();
    }

    public double getHeight() {
        return dimension.getHeight();
    }

    public void newWidth(double width) {
        dimension.setX(width);
    }

    public void newHeight(double height) {
        dimension.setY(height);
    }

    public void zoom(double factor) {
        this.zoom *= factor;
    }

    public void setup(GraphicsContext g) {
        g.scale(zoom, zoom);
        g.translate(-position.x, -position.y);
    }

    public void translate(double x, double y) {
        this.position.translate(new Point(x, y));
    }
}
