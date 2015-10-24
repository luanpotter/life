package xyz.luan.life;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.shape.Shape;
import xyz.luan.life.model.Entity;
import xyz.luan.life.model.Individual;

public class Game {

    private List<Entity> entities;
    private Dimension2D dimension;

    public Game(Dimension2D dimension, Group root) {
        this.entities = new ArrayList<Entity>();
        this.dimension = dimension;

        entities.add(Individual.abiogenesis());
        root.getChildren().add(entities.get(0).getBody());
    }

    public void tick(Group group) {
        for (Entity entity : entities) {
            entity.tick(entities);
            entity.fixPosition(dimension);
            for (Entity otherEntity : entities) {
                if (entity != otherEntity) {
                    Shape intersection = entity.intersects(otherEntity);
                    if (intersection.getLayoutBounds().getHeight() == 0 && intersection.getLayoutBounds().getWidth() == 0) {
                        entity.onCollide(otherEntity, intersection, group, entities);
                    }
                }
            }
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Dimension2D getDimension() {
        return dimension;
    }
}
