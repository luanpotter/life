package xyz.luan.life.model.genetics;

import javafx.geometry.Point2D;
import xyz.luan.life.model.EntityShape;
import xyz.luan.life.model.Util;

public class RotationGene implements Gene<RotationGene> {

	private static final double MAX = 0.1, MIN = 0, VARIANCE = 0.005;

	private double speed;

	private RotationGene(double speed) {
		this.speed = speed;
	}

	public RotationGene() {
		this.speed = 0.05;
	}

	public void rotate(EntityShape body) {
		Point2D velocity = body.getVelocity();
		double theta = speed * (Math.random() - 0.5);
		velocity = Util.rotate(velocity, theta);
		body.setVelocity(velocity);
		body.rotate(theta);
	}

	public void mutation() {
		this.speed += Math.random() * VARIANCE * (Math.random() > .5 ? 1 : -1);
		if (this.speed < MIN) {
			this.speed = 2 * MIN - this.speed;
		}
		if (this.speed > MAX) {
			this.speed = 2 * MAX - this.speed;
		}
	}

	@Override
	public RotationGene meiosis(RotationGene gene) {
		double speed = (this.speed + gene.speed) / 2;
		RotationGene childGene = new RotationGene(speed);
		if (Math.random() < Gene.MUTATION_PROBABILITY) {
			childGene.mutation();
		}
		return childGene;
	}
}
