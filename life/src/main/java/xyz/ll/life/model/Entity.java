package xyz.ll.life.model;

import javafx.scene.canvas.GraphicsContext;
import xyz.ll.life.EntityManager;
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
        return body.getShape().intersection(entity.body.getShape());
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

    public void draw(GraphicsContext g) {
        body.draw(g);
    }
}
