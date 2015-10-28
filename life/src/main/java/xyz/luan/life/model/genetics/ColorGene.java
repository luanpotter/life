package xyz.luan.life.model.genetics;

import javafx.scene.paint.Color;
import xyz.luan.life.model.EntityShape;
import xyz.luan.life.model.Util;

public class ColorGene implements Gene<ColorGene> {

	private static final double MAX = 2 * Math.PI, MIN = 0, VARIANCE = 0.1;

	private double theta;

	public ColorGene() {
		this(Math.random() * MAX);
	}

	private ColorGene(double theta) {
		this.theta = theta;
	}

	public void set(EntityShape body) {
		body.setColor(getColor());
	}

	private Color getColor() {
		return Color.hsb(Math.toDegrees(theta), Util.DEFAULT_INDIVIDUAL_COLOR_SATURATION, Util.DEFAULT_INDIVIDUAL_COLOR_VALUE);
	}

	public void mutation() {
		this.theta += Math.random() * VARIANCE * (Math.random() > .5 ? 1 : -1);
		if (this.theta < MIN) {
			this.theta = 2 * MIN - this.theta;
		}
		if (this.theta > MAX) {
			this.theta = 2 * MAX - this.theta;
		}
	}

	@Override
	public ColorGene meiosis(ColorGene gene) {
		double theta = (this.theta + gene.theta) / 2;
		ColorGene childGene = new ColorGene(theta);
		if (Math.random() < Gene.MUTATION_PROBABILITY) {
			childGene.mutation();
		}
		return childGene;
	}

}
