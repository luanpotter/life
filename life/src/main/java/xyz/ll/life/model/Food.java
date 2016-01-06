package xyz.ll.life.model;

import java.util.Arrays;

import javafx.scene.paint.Color;
import xyz.ll.life.EntityManager;
import xyz.ll.life.model.world.World;
import xyz.luan.geometry.Point;

public class Food extends Organic {

    private static final double ENERGY = 100000;
    private static final Color FOOD_COLOR = Color.hsb(0, 0, 0.2);
    private static final Point[] FOOD_VERTICES = { new Point(0, 1), new Point(1, 0), new Point(0, -1), new Point(-1, 0) };

    public Food(Point position, double energy) {
        super(Food.foodShape(position), energy);
    }

    public Food(Individual individual) {
        super(Food.convertBody(individual), 0);
    }

    public static Food randomFood(World world) {
        return new Food(world.randomPoint(), Food.ENERGY);
    }

    private static EntityShape convertBody(Individual individual) {
        EntityShape body = individual.getBody();
        body.setColor(Food.FOOD_COLOR);
        body.setStrokeColor(null);
        return body;
    }

    private static EntityShape foodShape(Point center) {
        EntityShape foodShape = new EntityShape(center, Arrays.asList(FOOD_VERTICES));
        foodShape.setColor(Food.FOOD_COLOR);
        return foodShape;
    }

    @Override
    public void tick(EntityManager em) {
    }

    @Override
    public void onCollide(Entity entity, EntityManager em) {
    }
}
