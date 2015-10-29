package xyz.ll.life.model;

import javafx.geometry.Dimension2D;
import javafx.scene.shape.Shape;
import xyz.ll.life.EntityManager;

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

	public boolean estimatedIntersects(Entity entity) {
		return body.getBoundsInParent().intersects(entity.body.getBoundsInParent());
	}

	public Shape intersects(Entity entity) {
		if (entity != null) {
			return Shape.intersect(body, entity.body);
		} else {
			return null;
		}
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
		body.fixPosition(d);
	}
}
