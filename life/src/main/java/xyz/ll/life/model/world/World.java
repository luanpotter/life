package xyz.ll.life.model.world;

import java.util.Random;

import xyz.ll.life.model.Entity;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Shape;

public class World {

    private Dimension borders;

    public World(Dimension borders) {
        this.borders = borders;
    }

    public Point randomPoint() {
        Random r = new Random();
        return new Point(r.nextInt((int) borders.getWidth()), r.nextInt((int) borders.getHeight()));
    }

    public void fixPosition(Entity e) {
        Shape shape = e.getBody().getShape();
        while (shape.getBounds().getX() + shape.getBounds().getWidth() < 0) {
            e.getBody().translate(borders.getWidth(), 0);
        }
        while (shape.getBounds().getX() > borders.getWidth()) {
            e.getBody().translate(-borders.getWidth(), 0);
        }
        while (shape.getBounds().getY() + shape.getBounds().getHeight() < 0) {
            e.getBody().translate(0, borders.getHeight());
        }
        while (shape.getBounds().getY() > borders.getHeight()) {
            e.getBody().translate(0, -borders.getHeight());
        }
    }

    public double area() {
        return borders.area();
    }
}
