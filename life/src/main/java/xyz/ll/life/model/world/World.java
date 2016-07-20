package xyz.ll.life.model.world;

import javafx.scene.canvas.GraphicsContext;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.Organic;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Shape;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class World {

    private Dimension borders;
    private List<Wall> walls;

    public World(Dimension borders) {
        this.borders = borders;
        this.walls = new ArrayList<>();
        WorldTypes.PROPPER_RING.build(this);
    }

    public Point randomPoint() {
        Random r = new Random();
        return new Point(r.nextInt((int) borders.getWidth()), r.nextInt((int) borders.getHeight()));
    }

    public void fixPosition(Organic e) {
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

        for (Wall wall : walls) {
            e.onCollide(wall, null); // TODO is em needed here (?)
        }
    }

    List<Wall> getWalls() {
        return this.walls;
    }

    Dimension getBorders() {
        return borders;
    }

    public double getWidth() {
        return borders.getWidth();
    }

    public double getHeight() {
        return borders.getHeight();
    }

    public void draw(GraphicsContext g) {
        walls.forEach(w -> w.draw(g));
    }

    public double area() {
        return borders.area();
    }

    public boolean collides(Entity e) {
        for (Wall wall : walls) {
            if (wall.intersection(e).area() > 0) {
                return true;
            }
        }
        return false;
    }
}
