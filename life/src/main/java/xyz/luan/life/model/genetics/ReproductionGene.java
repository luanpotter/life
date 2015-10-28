package xyz.luan.life.model.genetics;

import xyz.luan.life.model.EntityShape;
import xyz.luan.life.model.Individual;

public class ReproductionGene implements Gene<ReproductionGene> {

    private static double LIBIDO_MAX = 1, LIBIDO_MIN = 0, LIBIDO_VARIANCE = 0.05;
    private static double CHARITY_MIN = 0, CHARITY_VARIANCE = 0.2;

    public static final double BASE_REPRODUCTION_ENERGY_COST = 20d;

    private double libido;
    private double charity;

    private ReproductionGene(double libido, double charity) {
        this.libido = libido;
        this.charity = charity;
    }

    public ReproductionGene() {
        this.libido = 0.5d;
        this.charity = 2d;
    }

    public double reproductionCost(EntityShape body) {
        return body.estimateArea() * (ReproductionGene.BASE_REPRODUCTION_ENERGY_COST + this.charity);
    }

    public boolean isAvailableToReproduce(EntityShape body, double energy) {
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
        return null;
    }

    @Override
    public double distance(ReproductionGene gene) {
        double fl = ReproductionGene.LIBIDO_MAX - ReproductionGene.LIBIDO_MIN;
        double fc = 1;
        return Math.abs(this.libido - gene.libido) / fl +
                Math.abs(this.charity - gene.charity) / fc;
    }
}
