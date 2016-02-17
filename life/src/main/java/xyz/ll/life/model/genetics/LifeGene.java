package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

import java.util.Arrays;
import java.util.List;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class LifeGene implements Gene<LifeGene> {

    private static double BASE_COST_OF_LIVING = 0.1d;
    private static double OLDNESS_IMPACT = 0.0000001d;

    private static final Mutation HEALTH = Mutation.helper().min(0d).max(1d).variance(0.0000005d).build();

    private double health;

    private LifeGene(double health) {
        this.health = health;
    }

    public LifeGene() {
        this.health = 0.99999;
    }

    public boolean disease(int age) {
        return Math.random() > this.health * Util.positive(1 - age * LifeGene.OLDNESS_IMPACT);
    }

    public double lifeCost(EntityShape body) {
        return body.getArea() * LifeGene.BASE_COST_OF_LIVING * this.health;
    }

    @Override
    public void mutation() {
        this.health = HEALTH.mutate(health);
    }

    @Override
    public LifeGene meiosis(LifeGene gene) {
        double health = Util.random(this.health, gene.health);
        LifeGene childGene = new LifeGene(health);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(LifeGene gene) {
        return Math.abs(this.health - gene.health) / HEALTH.range();
    }

    @Override
    public List<Double> getValues() {
        return Arrays.asList(HEALTH.mutate(health));
    }
}
