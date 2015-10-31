package xyz.ll.life.model;

import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import xyz.ll.life.EntityManager;

public class Food extends Entity {

    private static final double ENERGY = 100000;
    private static final Color FOOD_COLOR = Color.hsb(0, 0, 0.2);
    private static final Point2D[] FOOD_VERTICES = { new Point2D(0, 1), new Point2D(1, 0), new Point2D(0, -1),
            new Point2D(-1, 0) };

    public Food(Point2D position, double energy) {
        super(Food.foodShape(position), energy);
    }

    public Food(Individual individual) {
        super(Food.convertBody(individual), 0);
    }

    public static Food randomFood(Dimension2D dimension) {
        Random r = new Random();
        return new Food(new Point2D(r.nextInt((int) dimension.getWidth()), r.nextInt((int) dimension.getHeight())),
                Food.ENERGY);
    }

    private static EntityShape convertBody(Individual individual) {
        EntityShape body = individual.getBody();
        body.setColor(Food.FOOD_COLOR);
        return body;
    }

    private static EntityShape foodShape(Point2D center) {
        EntityShape foodShape = new EntityShape(center);
        foodShape.setColor(Food.FOOD_COLOR);
        foodShape.setVertices(FOOD_VERTICES);
        return foodShape;
    }

    @Override
    public void tick(EntityManager em) {
    }

    @Override
    public void onCollide(Entity entity, EntityManager em) {
    }
}
