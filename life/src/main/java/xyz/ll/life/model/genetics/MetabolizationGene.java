package xyz.ll.life.model.genetics;

import xyz.ll.life.model.EntityShape;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class MetabolizationGene implements Gene<MetabolizationGene> {

    private static final double BASE_STRUCTURE_ENERGY = 1d;

    private static final Mutation EFFICIENCY = Mutation.helper().min(0d).max(0.9d).variance(0.05d).build();
    private static final Mutation AREA_PROPORTION = Mutation.helper().min(0d).max(0.6d).variance(0.05d).build();
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
        return this.efficiency * (preyEnergy + prey.getArea() * (MetabolizationGene.BASE_STRUCTURE_ENERGY - this.cost));
    }

    @Override
    public void mutation() {
        this.efficiency = EFFICIENCY.mutate(this.efficiency);
        this.areaProportion = AREA_PROPORTION.mutate(this.areaProportion);

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
        double effDist = Math.abs(this.efficiency - gene.efficiency) / EFFICIENCY.range();
        double areaDist = Math.abs(this.areaProportion - gene.areaProportion) / AREA_PROPORTION.range();
        double costDist = Math.abs(this.cost - gene.cost) * MetabolizationGene.COST_WEIGHT;
        return effDist + areaDist + costDist;
    }
}
