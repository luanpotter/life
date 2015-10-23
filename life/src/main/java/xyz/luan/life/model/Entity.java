package xyz.luan.life.model;

import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.List;

public abstract class Entity {

    private Polygon body;
    private double area;
    private double energy;

    public abstract void tick();
    public abstract void onCollide(Entity entity, Shape intersection, Group group, List<Entity> entities);

    public Entity(Polygon body, double energy) {
        this.body = body;
        this.energy = energy;
        this.area = Util.getArea(body);
    }

    public Shape intersects(Entity entity) {
        return Shape.intersect(body, entity.body);
    }

    public double die(Group group, List<Entity> entities) {
        group.getChildren().remove(body);
        entities.remove(this);
        return energy * Util.BASE_ENERGY_RELEASED;
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

    public Polygon getBody() {
        return body;
    }

    public double getArea() {
        return area;
    }
}
