package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.Food;
import xyz.ll.life.model.Individual;
import xyz.ll.life.model.Species;
import xyz.ll.life.model.world.Dimension;
import xyz.ll.life.model.world.Viewport;
import xyz.ll.life.model.world.World;

public class Game {

    private List<Entity> entities;
    private World world;
    private EntityManager em;

    private Viewport viewport;
    private Individual selected;
    private boolean rendering;

    public Game(Dimension dimension) {
        this.entities = new ArrayList<>();
        this.world = new World(dimension);
        this.viewport = new Viewport(dimension);
        this.em = new EntityManager();
        this.rendering = true;

        randomStart();
    }

    public boolean isRendering() {
        return rendering;
    }

    public void setRendering(boolean rendering) {
        this.rendering = rendering;
    }

    private void randomStart() {
        int foodAmount = (int) (0.000125 * world.area());
        int lifeAmount = (int) (0.000050 * world.area());
        for (int i = 0; i < foodAmount; i++) {
            entities.add(Food.randomFood(world));
        }
        for (int i = 0; i < lifeAmount; i++) {
            entities.add(Individual.abiogenesis(world, 4d));
        }
    }

    public synchronized void tick() {
        generateRandomFood();

        for (Entity e : entities) {
            if (em.deceased(e)) {
                continue;
            }

            setStroke(e);
            e.tick(em);
            world.fixPosition(e);
            dealWithCollisions(e, em);
        }

        evaluateEntityManager();
    }

    private void evaluateEntityManager() {
        for (Entity e : em.getRemoved()) {
            entities.remove(e);
        }
        for (Entity e : em.getAdded()) {
            entities.add(e);
        }
        em.clear();
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
        double prob = 5 * Math.pow(10, -7) * world.area();
        if (Math.random() < prob) {
            entities.add(Food.randomFood(world));
        }
    }

    public void remove(Entity e) {
        em.remove(e);
    }

    public void add(Entity e) {
        em.add(e);
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

    public World getWorld() {
        return world;
    }

    public Viewport getViewport() {
        return viewport;
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
        g.fillRect(0, 0, viewport.getWidth(), viewport.getHeight());
    }
}