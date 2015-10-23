package xyz.luan.life.model;

import com.sun.javafx.geom.FlatteningPathIterator;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.util.Random;

public class Individual extends Entity {

	private Genome genome;

    private static Polygon generateBody(Point2D position, Genome genome) {
        double SIZE = 4;
        Polygon body = new Polygon(position.getX() - SIZE,
                position.getY() - SIZE,
                position.getX() + SIZE,
                position.getY() - SIZE,
                position.getX() + SIZE,
                position.getY() + SIZE,
                position.getX() - SIZE,
                position.getY() + SIZE);
        return null;
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
        this.setEnergy(this.getEnergy() - amount);
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
                if (genome.get(Gene.LIBIDO) < (this.getEnergy() / cost)) {
                    return true;
                }
            } else {
                if (Util.DEFAULT_INDIVIDUAL_LIBIDO < (this.getEnergy() / cost)) {
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

    @Override
    public void onCollide(Entity entity, Shape intersection, Group group) {
        if (entity instanceof Food) {

        } else if (entity instanceof Individual) {
            if (((Individual) entity).isAvailableToReproduce()) {
                Individual child = reproduce((Individual) entity, intersection);
                group.getChildren().add(child.getBody());
            }
        }
    }

    @Override
    public void tick() {

    }
}
