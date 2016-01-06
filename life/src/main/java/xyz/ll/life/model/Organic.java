package xyz.ll.life.model;

import xyz.ll.life.EntityManager;
import xyz.luan.geometry.Shape;

public abstract class Organic extends Entity {

    protected double area;
    protected double energy;

    public abstract void tick(EntityManager em);

    public abstract void onCollide(Entity entity, EntityManager em);

    public Organic(EntityShape body, double energy) {
        this.body = body;
        this.energy = energy;
        this.area = body.getArea();
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

    public double getArea() {
        return area;
    }
}
