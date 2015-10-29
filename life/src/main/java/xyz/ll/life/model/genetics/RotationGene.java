package xyz.ll.life.model.genetics;

import javafx.geometry.Point2D;
import xyz.ll.life.model.EntityShape;

public class RotationGene implements Gene<RotationGene> {

    private static final double SPEED_MAX = 0.1d, SPEED_MIN = 0d, SPEED_VARIANCE = 0.005d;

    private double speed;

    private RotationGene(double speed) {
        this.speed = speed;
    }

    public RotationGene() {
        this.speed = 0.1;
    }

    public void rotate(EntityShape body) {
        Point2D velocity = body.getVelocity();
        double theta = speed * (Math.random() - 0.5);
        velocity = Util.rotate(velocity, theta);
        body.setVelocity(velocity);
        body.rotate(theta);
    }

    public void mutation() {
        if (Math.random() < MUTATION_PROBABILITY) {
            this.speed += Math.random() * SPEED_VARIANCE * (Math.random() > .5 ? 1 : -1);
            if (this.speed < SPEED_MIN) {
                this.speed = 2 * SPEED_MIN - this.speed;
            }
            if (this.speed > SPEED_MAX) {
                this.speed = 2 * SPEED_MAX - this.speed;
            }
        }
    }

    @Override
    public RotationGene meiosis(RotationGene gene) {
        double speed = Util.random(this.speed, gene.speed);
        RotationGene childGene = new RotationGene(speed);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(RotationGene gene) {
        double fs = RotationGene.SPEED_MAX - RotationGene.SPEED_MIN;
        return Math.abs(this.speed - gene.speed) / fs;
    }
}
