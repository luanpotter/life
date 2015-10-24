package xyz.luan.life.model;

import com.sun.javafx.geom.FlatteningPathIterator;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.List;
import java.util.Map;
import java.util.Random;

public class Individual extends Entity {

	private Genome genome;

    private static EntityShape generateBody(Point2D position, Genome genome) {
        Color color = null;
        if (genome.getGenes().containsKey(Gene.COLOR)) {
            color = Util.COLORS[(int) genome.get(Gene.COLOR)];
        }

        double a, b, c, d, e ,f ,g, h, i, j, k, l, m, n, o;
        a = b = c = d = e = f = g = h = i = j = k = l = m = n = o = Util.DEFAULT_INDIVIDUAL_MORFOLOGY;

        Map<Gene, Double> genes = genome.getGenes();
        if (genes.containsKey(Gene.A)) {
            a = genome.get(Gene.A);
        }
        if (genes.containsKey(Gene.B)) {
            b = genome.get(Gene.B);
        }
        if (genes.containsKey(Gene.C)) {
            c = genome.get(Gene.C);
        }
        if (genes.containsKey(Gene.D)) {
            d = genome.get(Gene.D);
        }
        if (genes.containsKey(Gene.E)) {
            e = genome.get(Gene.E);
        }
        if (genes.containsKey(Gene.F)) {
            f = genome.get(Gene.F);
        }
        if (genes.containsKey(Gene.G)) {
            g = genome.get(Gene.G);
        }
        if (genes.containsKey(Gene.H)) {
            h = genome.get(Gene.H);
        }
        if (genes.containsKey(Gene.I)) {
            i = genome.get(Gene.I);
        }
        if (genes.containsKey(Gene.J)) {
            j = genome.get(Gene.J);
        }
        if (genes.containsKey(Gene.K)) {
            k = genome.get(Gene.K);
        }
        if (genes.containsKey(Gene.L)) {
            l = genome.get(Gene.L);
        }
        if (genes.containsKey(Gene.M)) {
            m = genome.get(Gene.M);
        }
        if (genes.containsKey(Gene.N)) {
            n = genome.get(Gene.N);
        }
        if (genes.containsKey(Gene.O)) {
            o = genome.get(Gene.O);
        }

        double[] characteristics = {a, b, c, d, e ,f ,g, h, i, j, k, l, m, n, o};
        return new EntityShape(position, characteristics, color);
    }

    private Individual(Point2D position, double energy, Genome genome) {
        super(Individual.generateBody(position, genome), energy);

        this.genome = genome;

        if (genome.getGenes().containsKey(Gene.COLOR)) {
            int color = (int) (genome.get(Gene.COLOR) % Util.COLORS.length);
            this.getBody().setFill(Util.COLORS[color]);
        } else {
            this.getBody().setFill(Util.DEFAULT_INDIVIDUAL_COLOR);
        }
    }

    public Genome getGenome() {
        return genome;
    }

    public double sharedEnergy() {
        double amount = this.getArea() * genome.get(Gene.CHARITY);
        this.loseEnergy(amount);
        return amount;
    }

    public boolean isAvailableToReproduce() {
        double cost = this.getArea() * Util.BASE_REPRODUCTION_ENERGY_COST;
        if (genome.getGenes().containsKey(Gene.CHARITY)) {
            cost += this.getArea() * genome.get(Gene.CHARITY);
        } else {
            cost += this.getArea() * Util.DEFAULT_INDIVIDUAL_CHARITY;
        }
        if (this.getEnergy() >=  cost) {
            if (genome.getGenes().containsKey(Gene.LIBIDO)) {
                if (genome.get(Gene.LIBIDO) <= (this.getEnergy() / cost)) {
                    return true;
                }
            } else {
                if (Util.DEFAULT_INDIVIDUAL_LIBIDO <= (this.getEnergy() / cost)) {
                    return true;
                }
            }
        }
        return false;
    }

    private Individual reproduce(Individual pair, Shape intersection) {
        if (genome.geneticDistance(pair.genome) < Util.ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE) {
            Random random = new Random();
            Genome genome = new Genome();
            for (Gene gene : this.getGenome().getGenes().keySet()) {
                double a = this.getGenome().get(gene);
                double b = pair.getGenome().get(gene);
                double diff = Math.abs(a - b);
                double mix = Math.min(a, b) + diff * random.nextDouble();
                if (random.nextInt(Util.RARITY_OF_IMMUTABILITY) == 0) {
                    mix = mix + random.nextDouble() * Math.pow(-1, random.nextInt(1));
                }
                genome.getGenes().put(gene, Math.abs(mix));
            }

            double initialEnergy = this.sharedEnergy() + pair.sharedEnergy();
            Bounds bounds = intersection.getBoundsInParent();
            Point2D center = new Point2D((bounds.getMaxX() + bounds.getMinX()) / 2,
                    (bounds.getMaxY() + bounds.getMinY()) / 2);
            Individual child = new Individual(center, initialEnergy, genome);
            return child;
        } else {
            return null;
        }
    }

    private void die(List<Entity> entities) {
        Food food = new Food(this);
        entities.remove(this);
        entities.add(food);
    }

    @Override
    public void onCollide(Entity entity, Shape intersection, Group group, List<Entity> entities) {
        if (entity instanceof Individual) {
            if (((Individual) entity).isAvailableToReproduce()) {
                Individual child = reproduce((Individual) entity, intersection);
                entities.add(child);
                group.getChildren().add(child.getBody());
            }
        }
        if (Util.ACCEPTABLE_AREA_PROPORTION_TO_EAT > this.getArea() / entity.getArea() ) {
            double cost = Util.BASE_METABOLIZATION_ENERGY_COST * entity.getArea();
            if (this.getEnergy() >= cost) {
                this.loseEnergy(cost);
                this.gainEnergy(entity.eaten(group, entities));
            }
        }
    }

    @Override
    public void tick(List<Entity> entities) {
        this.loseEnergy(Util.BASE_LIFE_ENERGY_COST * this.getArea());
        if (this.getEnergy() < 0) {
            die(entities);
        }
    }
}
