package xyz.luan.life.model;

import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.shape.Shape;

public abstract class Entity {

    protected EntityShape body;
    protected double area;
    protected double energy;

    public abstract boolean tick();

    public abstract void onCollide(Entity entity, Shape intersection, Group group, List<Entity> entities);

    public abstract Entity onDeath();

    public Entity(EntityShape body, double energy) {
        this.body = body;
        this.energy = energy;
        this.area = body.estimateArea();
    }

    public Shape intersects(Entity entity) {
        if (entity != null && body.getBoundsInParent().intersects(entity.body.getBoundsInParent())) {
            return Shape.intersect(body, entity.body);
        } else {
            return null;
        }
    }

    public double eaten(Group group, List<Entity> entities) {
        group.getChildren().remove(body);
        entities.remove(this);
        return energy * Util.BASE_ENERGY_RELEASED + area * Util.BASE_STRUCTURE_ENERGY;
    }

    public double getEnergy() {
        return energy;
    }

    public double getTotalEnergy() {
        return energy * Util.BASE_ENERGY_RELEASED + area * Util.BASE_STRUCTURE_ENERGY;
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
        if (body.getCenter().getX() < 0) {
            body.translate(d.getWidth(), 0);
        } else if (body.getCenter().getX() > d.getWidth()) {
            body.translate(-d.getWidth(), 0);
        } else if (body.getCenter().getY() < 0) {
            body.translate(0, d.getHeight());
        } else if (body.getCenter().getY() > d.getHeight()) {
            body.translate(0, -d.getHeight());
        }
    }
}
