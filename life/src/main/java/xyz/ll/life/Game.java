package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.Food;
import xyz.ll.life.model.Individual;

public class Game {

    private Group root;
    private List<Entity> entities;
    private Dimension2D dimension;

    private Individual selected;

    public Game(Dimension2D dimension, Group root) {
        this.entities = new ArrayList<>();
        this.root = root;
        this.dimension = dimension;

        randomStart(dimension);
    }

    private void randomStart(Dimension2D dimension) {
        for (int i = 0; i < 10; i++) {
            add(Food.randomFood(dimension));
        }
        for (int i = 0; i < 10; i++) {
            add(Individual.abiogenesis(dimension, 4d));
        }
    }

    public void tick(Group group) {
        generateRandomFood();

        EntityManager em = new EntityManager();
        for (Entity e : entities) {
            if (!em.alive(e)) {
                continue;
            }

            if (e instanceof Individual) {
                if (selected == null && Color.RED.equals(e.getBody().getColorStroke())) {
                    e.getBody().setColorStroke(null);
                }
                if (selected != null) {
                    if (selected.getGenome().isCompatible(((Individual) e).getGenome())) {
                        e.getBody().setColorStroke(Color.RED);
                    } else {
                        e.getBody().setColorStroke(null);
                    }
                }
            }

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

    private void dealWithCollisions(Entity e, EntityManager em) {
        if (!(e instanceof Food)) {
            for (Entity otherEntity : entities) {
                if (e != otherEntity) {
                    if (e.estimatedIntersects(otherEntity)) {
                        e.onCollide(otherEntity, em);
                    }
                }
            }
        }
    }

    private void generateRandomFood() {
        if (Math.random() < 0.1) {
            add(Food.randomFood(dimension));
        }
    }

    public void remove(Entity e) {
        entities.remove(e);
        root.getChildren().remove(e.getBody());
    }

    public void add(Entity e) {
        entities.add(e);
        root.getChildren().add(e.getBody());
        if (e instanceof Individual) {
            e.getBody().toFront();
        } else {
            e.getBody().toBack();
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
}
