package xyz.luan.life.model;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.geometry.Bounds;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Individual extends Entity {

    private Genome genome;
    private Point2D velocity;

    private static EntityShape generateBody(Point2D position, Genome genome, int precision) {
        Color color = null;
        if (genome.getGenes().containsKey(Gene.COLOR)) {
            color = Util.COLORS[(int) (genome.get(Gene.COLOR) % Util.COLORS.length)];
        }

        List<Gene> morfologicalGenes = Arrays.asList(Gene.A, Gene.B, Gene.C, Gene.D, Gene.E, Gene.F, Gene.G, Gene.H, Gene.I, Gene.J, Gene.K, Gene.L, Gene.M,
                Gene.N);
        double[] characteristics = morfologicalGenes.stream().map(g -> {
            return genome.getGenes().containsKey(g) ? genome.getGenes().get(g) : Util.DEFAULT_INDIVIDUAL_MORFOLOGY;
        }).mapToDouble(Double::doubleValue).toArray();
        return new EntityShape(position, characteristics, color, precision);
    }

    public static Individual abiogenesis(Dimension2D dimension) {
        Random r = new Random();
        return new Individual(new Point2D(r.nextInt((int) dimension.getWidth()), r.nextInt((int) dimension.getHeight())), 50000, new Genome());
    }

    private Individual(Point2D position, double energy, Genome genome) {
        super(Individual.generateBody(position, genome, 100), energy);

        this.genome = genome;
        this.velocity = new Point2D(Math.sqrt(2) * genome.get(Gene.TRANSLATION_SPEED) / 75, Math.sqrt(2) * genome.get(Gene.TRANSLATION_SPEED) / 75);
        this.velocity = Util.rotate(this.velocity, Math.random() * 360);

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
        if (this.getEnergy() >= cost) {
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
            Point2D center = new Point2D((bounds.getMaxX() + bounds.getMinX()) / 2, (bounds.getMaxY() + bounds.getMinY()) / 2);
            Individual child = new Individual(center, initialEnergy, genome);
            return child;
        } else {
            return null;
        }
    }

    @Override
    public Food onDeath() {
        return new Food(this);
    }

    @Override
    public void onCollide(Entity entity, Shape intersection, Group group, List<Entity> entities) {
        if (this.isAvailableToReproduce() && entity instanceof Individual) {
            if (((Individual) entity).isAvailableToReproduce()) {
                Individual child = reproduce((Individual) entity, intersection);
                if (child != null) {
                    entities.add(child);
                    group.getChildren().add(child.getBody());
                }
            }
        }
        if (Util.ACCEPTABLE_AREA_PROPORTION_TO_EAT <  this.getArea() / entity.getArea()) {
            double cost = Util.BASE_METABOLIZATION_ENERGY_COST * entity.getArea();
            if (this.getTotalEnergy() >= cost) {
                this.loseEnergy(cost);
                this.gainEnergy(entity.eaten(group, entities));
            }
        }
    }

    @Override
    public boolean tick() {
        this.loseEnergy(Util.BASE_LIFE_ENERGY_COST * this.getArea());
        if (this.getEnergy() < 0) {
            return false;
        }

        this.move();
        return true;
    }

    private void move() {
        int sign = Math.random() > 0.5 ? 1 : -1;
        velocity = Util.rotate(this.velocity, sign * Math.random() * 180 / genome.get(Gene.TRANSLATION_CONSTANCY) / 50);
        body.translate(velocity.getX(), velocity.getY());
    }

}
