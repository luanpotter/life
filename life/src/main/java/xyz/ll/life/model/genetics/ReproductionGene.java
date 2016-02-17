package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

import java.util.Arrays;
import java.util.List;

public class ReproductionGene implements Gene<ReproductionGene> {

    private static final Mutation LIBIDO = Mutation.helper().min(0d).max(1d).variance(0.05d).build();
    private static final Mutation CHARITY = Mutation.helper().min(0d).max(10d).variance(0.2d).build();

    private static final double BASE_REPRODUCTION_ENERGY_COST = 40d;

    private double libido;
    private double charity;

    private ReproductionGene(double libido, double charity) {
        this.libido = libido;
        this.charity = charity;
    }

    public ReproductionGene() {
        this.libido = 0.1;
        this.charity = 40d;
    }

    public static double meiosisCost(EntityShape body) {
        return body.getArea() * ReproductionGene.BASE_REPRODUCTION_ENERGY_COST;
    }

    public double careCost(EntityShape body) {
        return body.getArea() * this.charity;
    }

    public double reproductionCost(EntityShape body) {
        return meiosisCost(body) + careCost(body);
    }

    public boolean available(EntityShape body, double energy) {
        double amount = energy * this.libido;
        if (amount >= reproductionCost(body)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void mutation() {
        this.libido = LIBIDO.mutate(this.libido);
        this.charity = CHARITY.mutate(this.charity);
    }

    @Override
    public ReproductionGene meiosis(ReproductionGene gene) {
        double libido = Util.random(this.libido, gene.libido);
        double charity = Util.random(this.charity, gene.charity);
        ReproductionGene childGene = new ReproductionGene(libido, charity);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(ReproductionGene gene) {
        double libidoDist = Math.abs(this.libido - gene.libido) / LIBIDO.range();
        double charityDist = Math.abs(this.charity - gene.charity) / CHARITY.range();
        return libidoDist + charityDist;
    }

    @Override
    public List<Double> getValues() {
        return Arrays.asList(LIBIDO.normalize(libido), CHARITY.normalize(charity));
    }
}
