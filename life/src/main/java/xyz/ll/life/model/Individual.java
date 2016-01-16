package xyz.ll.life.model;

import java.util.List;

import javafx.scene.paint.Color;
import xyz.ll.life.EntityManager;
import xyz.ll.life.model.genetics.Genome;
import xyz.ll.life.model.world.Wall;
import xyz.ll.life.model.world.World;
import xyz.luan.geometry.Line;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Shape;

public class Individual extends Organic {

    private Genome genome;

    private int tickAge = 0;
    private long timeAge = System.currentTimeMillis();
    private int generation = 0;

    private Individual(Point position, double energy, Genome genome) {
        super(Individual.generateBody(position, genome), energy);

        this.genome = genome;
    }

    public static Individual abiogenesis(World world, double size) {
        return new Individual(world.randomPoint(), 50000, new Genome(size));
    }

    public static Individual abiogenesis(Point p, double size) {
        return new Individual(p, 50000, new Genome(size));
    }

    private static EntityShape generateBody(Point position, Genome genome) {
        EntityShape body = genome.getMorfological().generateShape(position);
        genome.getTranslation().initialSpeed(body);
        genome.getRotation().initialAngularVelocity(body);
        genome.getColor().dye(body);
        return body;
    }

    public double divide() {
        double amount = genome.getReproduction().careCost(body);
        this.loseEnergy(genome.getReproduction().reproductionCost(body));
        return amount;
    }

    public boolean isAvailableToReproduce() {
        return genome.getReproduction().available(body, energy);
    }

    private void tryToReproduce(Entity entity, EntityManager em, LazyIntersection intersection) {
        if (entity instanceof Individual) {
            tryToReproduceIndividual((Individual) entity, em, intersection);
        }
    }

    private void tryToReproduceIndividual(Individual individual, EntityManager em, LazyIntersection intersection) {
        if (this.isAvailableToReproduce() && individual.isAvailableToReproduce()) {
            if (this.genome.isCompatible(individual.genome)) {
                if (intersection.intersects()) {
                    em.add(reproduce(individual, intersection.getShape()));
                }
            }
        }
    }

    private Individual reproduce(Individual pair, Shape intersection) {
        double initialEnergy = this.divide() + pair.divide();
        Point center = intersection.getBounds().getCenter();
        Individual child = new Individual(center, initialEnergy, this.genome.meiosis(pair.genome));
        child.generation = Math.max(this.generation, pair.generation) + 1;
        return child;
    }

    private void tryToEat(Entity entity, EntityManager em, LazyIntersection intersection) {
        if (entity instanceof Organic) {
            if (this.genome.getMetabolization().canEat(this.body, entity.body, this.energy)) {
                if (intersection.intersects()) {
                    eat((Organic) entity, em, intersection);
                }
            } else if (entity instanceof Food) {
                // Shape result =
                // entity.getBody().getShape().diff(this.getBody().getShape());
                // if (Math.random() < 0.01) {
                // entity.body.setShape(result);
                // }
            }
        }
    }

    private void eat(Organic organic, EntityManager em, LazyIntersection intersection) {
        if (intersection.getShape().area() > organic.getArea() / 2) {
            this.gainEnergy(this.genome.getMetabolization().phagocytosis(organic.body, organic.energy));
            organic.getBody().setStrokeColor(Color.RED);
            em.remove(organic);
        }
    }

    private void move() {
        genome.getRotation().rotate(body);
        genome.getTranslation().translate(body);
        body.move();
    }

    private void live() {
        this.loseEnergy(this.genome.getLife().lifeCost(this.body));
    }

    private boolean disease() {
        return this.genome.getLife().disease(this.tickAge);
    }

    public Genome getGenome() {
        return this.genome;
    }

    private Food onDeath() {
        System.out.println("death { tick: " + tickAge + " time: " + (System.currentTimeMillis() - timeAge)
                + " generation: " + generation + " }");
        return new Food(this);
    }

    private void tryToAvoid(Entity entity, LazyIntersection intersection) {
        if (entity instanceof Wall && !this.body.getColor().equals(Color.AQUAMARINE)
                && !this.body.getColor().equals(Color.BEIGE)) {
            if (intersection.intersects()) {
                Point center = this.getBody().getCenter();
                Point velocity = this.body.getVelocity();
                Line line = Line.fromDirection(velocity, center);

                Shape wallShape = entity.getBody().getShape();
                List<Point> intersections = wallShape.intersections(line);
                intersections.sort((p1, p2) -> Double.compare(center.dist(p1), center.dist(p2)));
                Point point = intersections.get(0);

                Line side = wallShape.sides().stream().filter(s -> s.contains(point)).findAny().orElse(null);
                if (side == null) {
                    this.body.setColor(Color.BEIGE);
                    return;
                }
                double angle = side.getDirection().angle(velocity);
                Point reflectDirection = side.getDirection().scaleTo(-1).rotateTo(center, -angle).normal();

                this.body.setPosition(point);
                this.body.setVelocity(reflectDirection.scaleTo(velocity.magnitude()));
                this.body.setColor(Color.AQUAMARINE);
            }
        }
    }

    @Override
    public void onCollide(Entity entity, EntityManager em) {
        LazyIntersection intersection = new LazyIntersection(this, entity);

        tryToAvoid(entity, intersection);
        tryToReproduce(entity, em, intersection);
        tryToEat(entity, em, intersection);
    }

    @Override
    public void tick(EntityManager em) {
        this.tickAge++;
        live();

        if (disease() || this.getEnergy() < 0) {
            em.remove(this);
            em.add(onDeath());
            return;
        }

        move();
    }
}
