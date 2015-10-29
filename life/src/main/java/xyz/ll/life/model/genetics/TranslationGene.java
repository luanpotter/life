package xyz.ll.life.model.genetics;

import javafx.geometry.Point2D;
import xyz.ll.life.model.EntityShape;

public class TranslationGene implements Gene<TranslationGene> {

    private static final double SPEED_MAX = 1d, SPEED_MIN = 0d, SPEED_VARIANCE = 0.05d;
    private static final double INCONSTANCY_MAX = 1d, INCONSTANCY_MIN = 0d, INCONSTANCY_VARIANCE = 0.05d;

    private double speed;
    private double inconstancy;

    private TranslationGene(double speed, double inconstancy) {
        this.speed = speed;
        this.inconstancy = inconstancy;
    }

    public TranslationGene() {
        this.speed = 0.5;
        this.inconstancy = 0.5;
    }

    public void initialSpeed(EntityShape body) {
        Point2D velocity = new Point2D(this.speed, 0);
        velocity = Util.rotate(velocity, Math.random() * 2 * Math.PI);

        body.setVelocity(velocity);
    }

    public void translate(EntityShape body) {
        Point2D velocity = body.getVelocity();
        velocity = velocity.multiply(acceleration(velocity));
        body.setVelocity(velocity);
    }

    private double acceleration(Point2D velocity) {
        return (speed / velocity.magnitude()) + (2 * Math.random() - 1) * inconstancy;
    }

    @Override
    public void mutation() {
        this.speed += Math.random() * TranslationGene.SPEED_VARIANCE * (Math.random() > .5 ? 1 : -1);
        this.inconstancy += Math.random() * TranslationGene.INCONSTANCY_VARIANCE * (Math.random() > .5 ? 1 : -1);
        if (this.speed < TranslationGene.SPEED_MIN) {
            this.speed = 2 * TranslationGene.SPEED_MIN - this.speed;
        }
        if (this.speed > TranslationGene.SPEED_MAX) {
            this.speed = 2 * TranslationGene.SPEED_MAX - this.speed;
        }
        if (this.inconstancy < TranslationGene.INCONSTANCY_MIN) {
            this.inconstancy = 2 * TranslationGene.INCONSTANCY_MIN - this.inconstancy;
        }
        if (this.inconstancy > TranslationGene.INCONSTANCY_MAX) {
            this.inconstancy = 2 * TranslationGene.INCONSTANCY_MAX - this.inconstancy;
        }
    }

    @Override
    public TranslationGene meiosis(TranslationGene gene) {
        double speed = Util.random(this.speed, gene.speed);
        double constancy = Util.random(this.inconstancy, gene.inconstancy);
        TranslationGene childGene = new TranslationGene(speed, constancy);
        if (Math.random() < Gene.MUTATION_PROBABILITY) {
            childGene.mutation();
        }
        return childGene;
    }

    @Override
    public double distance(TranslationGene gene) {
        double fs = TranslationGene.SPEED_MAX - TranslationGene.SPEED_MIN;
        double fi = TranslationGene.INCONSTANCY_MAX - TranslationGene.INCONSTANCY_MIN;
        return Math.abs(this.speed - gene.speed) / fs + Math.abs(this.inconstancy - gene.inconstancy) / fi;
    }
}
