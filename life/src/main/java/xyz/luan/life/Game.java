package xyz.luan.life;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.shape.Shape;
import xyz.luan.life.model.Entity;
import xyz.luan.life.model.Food;
import xyz.luan.life.model.Individual;

public class Game {

    private List<Entity> entities;
    private Dimension2D dimension;
    private static final Random rand = new Random();

    public Game(Dimension2D dimension, Group root) {
        this.entities = new ArrayList<>();
        this.dimension = dimension;

        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));
        entities.add(Individual.abiogenesis(dimension));

        for (int i = 0; i < 10; i ++) {
            entities.add(Food.abiogenesis(dimension));
        }

        for (Entity e : entities) {
            root.getChildren().add(e.getBody());
        }
    }

    public void tick(Group group) {
        if (rand.nextInt(100) == 0) {
            Food f = Food.abiogenesis(dimension);
            entities.add(f);
            group.getChildren().add(f.getBody());
        }
        Iterator<Entity> it = entities.iterator();
        List<Entity> newEntites = new ArrayList<>();
        while (it.hasNext()) {
            Entity entity = it.next();
            boolean alive = entity.tick();
            if (!alive) {
                it.remove();
                newEntites.add(entity.onDeath());
            }
            entity.fixPosition(dimension);
            for (Entity otherEntity : entities) {
                if (entity != otherEntity) {
                    try {
                        Shape intersection = entity.intersects(otherEntity);
                        if (intersection != null && intersection.getLayoutBounds().getHeight() > 0 && intersection.getLayoutBounds().getWidth() > 0) {
                            entity.onCollide(otherEntity, intersection, group, entities);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        newEntites.stream().filter(e -> e != null).forEach(e -> entities.add(e));
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Dimension2D getDimension() {
        return dimension;
    }
}
