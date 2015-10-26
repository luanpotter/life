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
    private int tickAge = 0;
    private long timeAge = System.currentTimeMillis();
    private int generation = 0;

    private static class SendShape {

        private Shape shape;

        public Shape getShape() {
            return shape;
        }

        public void setShape(Shape shape) {
            this.shape = shape;
        }

        public SendShape(Shape shape) {
            this.shape = shape;
        }
    }

    private static EntityShape generateBody(Point2D position, Genome genome, int precision) {
        Color color = null;
        if (genome.getGenes().containsKey(Gene.COLOR)) {
            color = Color.hsb(Math.toDegrees(genome.get(Gene.COLOR)), Util.DEFAULT_INDIVIDUAL_COLOR_SATURATION, Util.DEFAULT_INDIVIDUAL_COLOR_VALUE);
        } else {
            color = Color.hsb(Util.DEFAULT_INDIVIDUAL_COLOR_HUE, Util.DEFAULT_INDIVIDUAL_COLOR_SATURATION, Util.DEFAULT_INDIVIDUAL_COLOR_VALUE);
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
        this.velocity = new Point2D(Math.sqrt(2) * genome.get(Gene.TRANSLATION_SPEED), Math.sqrt(2) * genome.get(Gene.TRANSLATION_SPEED));
        this.velocity = Util.rotate(this.velocity, Math.random() * 360);
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

        double cost = this.getArea() * Util.BASE_REPRODUCTION_ENERGY_COST;
        this.loseEnergy(cost);
        pair.loseEnergy(cost);

        double initialEnergy = this.sharedEnergy() + pair.sharedEnergy();
        Bounds bounds = intersection.getBoundsInParent();
        Point2D center = new Point2D((bounds.getMaxX() + bounds.getMinX()) / 2, (bounds.getMaxY() + bounds.getMinY()) / 2);
        Individual child = new Individual(center, initialEnergy, genome);
        child.generation = Math.max(this.generation, pair.generation) + 1;
        return child;
    }

    public boolean tryToReproduce(Entity entity, Group group, List<Entity> entities, SendShape intersection) {
        if (this.isAvailableToReproduce() && entity instanceof Individual) {
            if (((Individual) entity).isAvailableToReproduce()
                    && genome.geneticDistance(((Individual) entity).genome) < Util.ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE) {
                if (intersection.getShape() == null) {
                    intersection.setShape(this.intersects(entity));
                }
                if (intersection.getShape() != null && intersection.getShape().getLayoutBounds().getHeight() > 0 && intersection.getShape().getLayoutBounds().getWidth() > 0) {
                    Individual child = reproduce((Individual) entity, intersection.getShape());
                    entities.add(child);
                    group.getChildren().add(child.getBody());
                    return true;
                }
            }
        }
        return false;
    }

    public boolean tryToEat(Entity entity, Group group, List<Entity> entities, SendShape intersection) {
        if (Util.ACCEPTABLE_AREA_PROPORTION_TO_EAT <  this.getArea() / entity.getArea()) {
            double cost = Util.BASE_METABOLIZATION_ENERGY_COST * entity.getArea();
            if (this.getTotalEnergy() >= cost) {
                if (intersection.getShape() == null) {
                    intersection.setShape(this.intersects(entity));
                }
                if (intersection.getShape() != null && intersection.getShape().getLayoutBounds().getHeight() > 0 && intersection.getShape().getLayoutBounds().getWidth() > 0) {
                    this.loseEnergy(cost);
                    this.gainEnergy(entity.eaten(group, entities));
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public Food onDeath() {
        System.out.println("death { tick: " + tickAge + " time: " + (System.currentTimeMillis() - timeAge) + " generation: " + generation + " }");
        return new Food(this);
    }

    @Override
    public void onCollide(Entity entity, Group group, List<Entity> entities) {
        SendShape intersection = new SendShape(null);
        if (!tryToReproduce(entity, group, entities, intersection)) {
            tryToEat(entity, group, entities, intersection);
        }
    }

    @Override
    public boolean tick() {
        tickAge++;
        this.loseEnergy(Util.BASE_LIFE_ENERGY_COST * this.getArea());
        if (this.getEnergy() < 0) {
            return false;
        }

        this.move();
        return true;
    }

    private void move() {
        int sign = Math.random() > 0.5 ? 1 : -1;
        double angle = sign * Math.random() * 180 / genome.get(Gene.TRANSLATION_CONSTANCY);
        velocity = Util.rotate(this.velocity, angle);
        body.rotate(angle);
        body.translate(velocity.getX(), velocity.getY());
    }

}
