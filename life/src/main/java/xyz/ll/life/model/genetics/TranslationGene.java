package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;
import xyz.luan.geometry.Point;

import java.util.Arrays;
import java.util.List;

public class TranslationGene implements Gene<TranslationGene> {

    private static final Mutation SPEED = Mutation.helper().min(0d).max(1d).variance(0.05d).build();
    private static final Mutation INCONSTANCY = Mutation.helper().min(0d).max(1d).variance(0.05d).build();

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
        body.setVelocity(new Point(this.speed, 0).rotateTo(Math.random() * 2 * Math.PI));
    }

    public void translate(EntityShape body) {
        body.getVelocity().scale(acceleration(body.getVelocity()));
    }

    private double acceleration(Point velocity) {
        return (speed / velocity.magnitude()) + Math.random() * inconstancy;
    }

    @Override
    public void mutation() {
        this.speed = SPEED.mutate(this.speed);
        this.inconstancy = INCONSTANCY.mutate(this.inconstancy);
    }

    @Override
    public TranslationGene meiosis(TranslationGene gene) {
        double speed = Util.random(this.speed, gene.speed);
        double constancy = Util.random(this.inconstancy, gene.inconstancy);
        TranslationGene childGene = new TranslationGene(speed, constancy);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(TranslationGene gene) {
        double speedDistance = Math.abs(this.speed - gene.speed) / SPEED.range();
        double inconstancyDistance = Math.abs(this.inconstancy - gene.inconstancy) / INCONSTANCY.range();
        return speedDistance + inconstancyDistance;
    }

    @Override
    public List<Double> getValues() {
        return Arrays.asList(SPEED.normalize(speed), INCONSTANCY.normalize(inconstancy));
    }
}
