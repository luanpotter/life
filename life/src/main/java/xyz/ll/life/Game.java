package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.Food;
import xyz.ll.life.model.Individual;
import xyz.ll.life.model.Species;

public class Game {

    private List<Entity> entities;
    private Dimension2D dimension;

    private Individual selected;

    public Game(Dimension2D dimension) {
        this.entities = new ArrayList<>();
        this.dimension = dimension;

        randomStart();
    }

    private void randomStart() {
        int foodAmount = (int) (0.000125 * area());
        int lifeAmount = (int) (0.000050 * area());
        for (int i = 0; i < foodAmount; i++) {
            add(Food.randomFood(dimension));
        }
        for (int i = 0; i < lifeAmount; i++) {
            add(Individual.abiogenesis(dimension, 4d));
        }
    }

    private double area() {
        return dimension.getWidth() * dimension.getHeight();
    }

    public void tick() {
        generateRandomFood();

        EntityManager em = new EntityManager();
        for (Entity e : entities) {
            if (!em.alive(e)) {
                continue;
            }

            setStroke(e);
            e.tick(em);
            e.fixPosition(dimension);
            dealWithCollisions(e, em);
        }

        for (Entity e : em.getRemoved()) {
            remove(e);
        }
        for (Entity e : em.getAdded()) {
            add(e);
        }
    }

    private void setStroke(Entity e) {
        if (e instanceof Individual) {
            if (selected == null && Color.RED.equals(e.getBody().getStrokeColor())) {
                e.getBody().setStrokeColor(null);
            }
            if (selected != null) {
                if (selected.getGenome().isCompatible(((Individual) e).getGenome())) {
                    e.getBody().setStrokeColor(Color.RED);
                } else {
                    e.getBody().setStrokeColor(null);
                }
            }
        }
    }

    private void dealWithCollisions(Entity e, EntityManager em) {
        if (!(e instanceof Food)) {
            for (Entity otherEntity : entities) {
                if (e != otherEntity) {
                    e.onCollide(otherEntity, em);
                }
            }
        }
    }

    private void generateRandomFood() {
        double prob = Math.pow(10, -7) * area();
        if (Math.random() < prob) {
            add(Food.randomFood(dimension));
        }
    }

    public void remove(Entity e) {
        entities.remove(e);
    }

    public void add(Entity e) {
        entities.add(e);
    }

    public List<Species> species() {
        List<Species> species = new ArrayList<>();
        numberOfSpeciesRecursive(individuals(), species);
        return species;
    }

    private Stream<Individual> individuals() {
        return entities.stream().filter(e -> e instanceof Individual).map(e -> (Individual) e);
    }

    private Stream<Food> food() {
        return entities.stream().filter(e -> e instanceof Food).map(e -> (Food) e);
    }

    private static void numberOfSpeciesRecursive(Stream<Individual> individuals, List<Species> speciesList) {
        Species species = new Species();
        List<Individual> remains = new ArrayList<>();
        individuals.forEach(e -> {
            if (species.matches(e)) {
                species.add(e);
            } else {
                remains.add(e);
            }
        });
        if (species.size() > 0) {
            speciesList.add(species);
            numberOfSpeciesRecursive(remains.stream(), speciesList);
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Dimension2D getDimension() {
        return dimension;
    }

    public void setDimension(Dimension2D dimension) {
        this.dimension = dimension;
    }

    public Individual getSelected() {
        return selected;
    }

    public void setSelected(Individual selected) {
        this.selected = selected;
    }

    public void render(GraphicsContext g) {
        drawBackground(g);
        food().forEach(e -> e.draw(g));
        individuals().forEach(e -> e.draw(g));
    }

    private void drawBackground(GraphicsContext g) {
        g.setFill(Color.BLACK);
        g.fillRect(0, 0, dimension.getWidth(), dimension.getHeight());
    }
}