package xyz.luan.life.model;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.awt.geom.Point2D;
import java.util.List;


public class Food extends Entity {

    private static final double SIZE = 2;

    private static EntityShape generateBody(Point2D position) {
        return null;
    }

    private static EntityShape generateBody(Individual individual) {
        EntityShape body = individual.getBody();
        body.setFill(Util.DEFAULT_FOOD_COLOR);
        return body;
    }

    public Food(Point2D position, double energy) {
        super(Food.generateBody(position), energy);
    }

    public Food(Individual individual) {
        super(Food.generateBody(individual), 0);
    }

    @Override
    public void tick(List<Entity> entities) {

    }

    @Override
    public void onCollide(Entity entity, Shape intersection, Group group, List<Entity> entities) {

    }
}
