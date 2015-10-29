package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

public class ReproductionGene implements Gene<ReproductionGene> {

    private static double LIBIDO_MAX = 1d, LIBIDO_MIN = 0d, LIBIDO_VARIANCE = 0.05d;
    private static double CHARITY_MIN = 0d, CHARITY_WEIGHT = 2d, CHARITY_VARIANCE = 0.2d;

    public static final double BASE_REPRODUCTION_ENERGY_COST = 40d;

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
        this.libido += Math.random() * ReproductionGene.LIBIDO_VARIANCE * (Math.random() > 5 ? 1 : -1);
        this.charity += Math.random() * ReproductionGene.CHARITY_VARIANCE * (Math.random() > 5 ? 1 : -1);

        if (this.libido < ReproductionGene.LIBIDO_MIN) {
            this.libido = 2 * ReproductionGene.LIBIDO_MIN - this.libido;
        }
        if (this.libido > ReproductionGene.LIBIDO_MAX) {
            this.libido = 2 * ReproductionGene.LIBIDO_MAX - this.libido;
        }
        if (this.charity < ReproductionGene.CHARITY_MIN) {
            this.charity = 2 * ReproductionGene.CHARITY_MIN - this.charity;
        }
    }

    @Override
    public ReproductionGene meiosis(ReproductionGene gene) {
        double libido = (this.libido + gene.libido) / 2;
        double charity = (this.charity + gene.charity) / 2;
        ReproductionGene childGene = new ReproductionGene(libido, charity);
        if (Math.random() < MUTATION_PROBABILITY) {
            childGene.mutation();
        }
        return childGene;
    }

    @Override
    public double distance(ReproductionGene gene) {
        double fl = ReproductionGene.LIBIDO_MAX - ReproductionGene.LIBIDO_MIN;
        double fc = ReproductionGene.CHARITY_WEIGHT;
        return Math.abs(this.libido - gene.libido) / fl + Math.abs(this.charity - gene.charity) / fc;
    }
}
