package xyz.ll.life.model;

import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import xyz.ll.life.EntityManager;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Shape;

public abstract class Entity {

    protected EntityShape body;
    protected double area;
    protected double energy;

    public abstract void tick(EntityManager em);

    public abstract void onCollide(Entity entity, EntityManager em);

    public Entity(EntityShape body, double energy) {
        this.body = body;
        this.energy = energy;
        this.area = body.getArea();
    }

    public Shape intersection(Entity entity) {
        return body.getPolygon().intersection(entity.body.getPolygon());
    }

    protected static Point randomPoint(Dimension2D dimension) {
        Random r = new Random();
        return new Point(r.nextInt((int) dimension.getWidth()), r.nextInt((int) dimension.getHeight()));
    }

    public double getEnergy() {
        return energy;
    }

    public void gainEnergy(double energy) {
        this.energy += energy;
    }

    public void loseEnergy(double energy) {
        this.energy -= energy;
    }

    public EntityShape getBody() {
        return body;
    }

    public double getArea() {
        return area;
    }

    public void fixPosition(Dimension2D d) {
        body.fixPosition(d);
    }

    public void draw(GraphicsContext g) {
        body.draw(g);
    }
}
