package xyz.luan.life.model;

import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

public abstract class Entity {

    private Polygon body;
    private double energy;

    public abstract void tick();

    public boolean intersects(Entity entity) {
        Shape intersection = Shape.intersect(body, entity.body);
        if(intersection.getLayoutBounds().getHeight() == 0 && intersection.getLayoutBounds().getWidth() == 0) {
            return false;
        } else {
            return true;
        }
    }

    public double getEnergy() {
        return energy;
    }

    public void setEnergy(double energy) {
        this.energy = energy;
    }

    public Polygon getBody() {
        return body;
    }
}
