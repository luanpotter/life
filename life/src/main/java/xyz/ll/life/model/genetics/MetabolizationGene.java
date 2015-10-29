package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class MetabolizationGene implements Gene<MetabolizationGene> {

    private static final double BASE_STRUCTURE_ENERGY = 1d;

    private static final double EFFICIENCY_MAX = 0.9d, EFFICIENCY_MIN = 0d, EFFICIENCY_VARIANCE = 0.05d;
    private static final double AREA_PROPORTION_MAX = 0.6d, AREA_PROPORTION_MIN = 0d, AREA_PROPORTION_VARIANCE = 0.05d;
    private static final double COST_MIN = 0.1d, COST_WEIGHT = 1, COST_VARIANCE = 0.05d;

    private double efficiency;
    private double areaProportion;
    private double cost;

    private MetabolizationGene(double efficiency, double areaProportion, double cost) {
        this.efficiency = efficiency;
        this.areaProportion = areaProportion;
        this.cost = cost;
    }

    public MetabolizationGene() {
        this.efficiency = 0.8;
        this.areaProportion = 0.3;
        this.cost = 0.2;
    }

    public boolean canEat(EntityShape predator, EntityShape prey, double predatorEnergy) {
        if (prey.getArea() / predator.getArea() < this.areaProportion) {
            return predatorEnergy >= prey.getArea() * this.cost;
        } else {
            return false;
        }
    }

    public double phagocytosis(EntityShape prey, double preyEnergy) {
        return this.efficiency *
                (preyEnergy + prey.getArea() * (MetabolizationGene.BASE_STRUCTURE_ENERGY - this.cost));
    }

    @Override
    public void mutation() {
        if (Math.random() < MUTATION_PROBABILITY) {
            this.efficiency += Math.random() * MetabolizationGene.EFFICIENCY_VARIANCE * (Math.random() > .5 ? 1 : -1);
            if (this.efficiency < MetabolizationGene.EFFICIENCY_MIN) {
                this.efficiency = 2 * MetabolizationGene.EFFICIENCY_MIN - this.efficiency;
            }
            if (this.efficiency > MetabolizationGene.EFFICIENCY_MAX) {
                this.efficiency = 2 * MetabolizationGene.EFFICIENCY_MAX - this.efficiency;
            }
        }

        if (Math.random() < MUTATION_PROBABILITY) {
            this.areaProportion += Math.random() * MetabolizationGene.AREA_PROPORTION_VARIANCE * (Math.random() > .5 ? 1 : -1);
            if (this.areaProportion < MetabolizationGene.AREA_PROPORTION_MIN) {
                this.areaProportion = 2 * MetabolizationGene.AREA_PROPORTION_MIN - this.areaProportion;
            }
            if (this.areaProportion > MetabolizationGene.AREA_PROPORTION_MAX) {
                this.areaProportion = 2 * MetabolizationGene.AREA_PROPORTION_MAX - this.areaProportion;
            }
        }

        if (Math.random() < MUTATION_PROBABILITY) {
            this.cost += Math.random() * MetabolizationGene.COST_VARIANCE * (Math.random() > .5 ? 1 : -1);
            if (this.cost < MetabolizationGene.COST_MIN) {
                this.cost = 2 * MetabolizationGene.COST_MIN - this.cost;
            }
        }
    }

    @Override
    public MetabolizationGene meiosis(MetabolizationGene gene) {
        double efficiency = Util.random(this.efficiency, gene.efficiency);
        double areaProportion = Util.random(this.areaProportion, gene.areaProportion);
        double cost = Util.random(this.cost, gene.cost);
        MetabolizationGene childGene = new MetabolizationGene(efficiency, areaProportion, cost);
        childGene.mutation();

        return childGene;
    }

    @Override
    public double distance(MetabolizationGene gene) {
        double fe = MetabolizationGene.EFFICIENCY_MAX - MetabolizationGene.EFFICIENCY_MIN;
        double fa = MetabolizationGene.AREA_PROPORTION_MAX - MetabolizationGene.AREA_PROPORTION_MIN;
        double fc = 1 / MetabolizationGene.COST_WEIGHT;
        return Math.abs(this.efficiency - gene.efficiency) / fe +
                Math.abs(this.areaProportion - gene.areaProportion) / fa +
                Math.abs(this.cost - gene.cost) / fc;
    }
}
