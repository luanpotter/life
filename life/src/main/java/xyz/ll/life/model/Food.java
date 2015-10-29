package xyz.ll.life.model;

import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import xyz.ll.life.EntityManager;

public class Food extends Entity {

	private static final double SIZE = 2d;
    public static final Color FOOD_COLOR = Color.GRAY;

	public static Food randomFood(Dimension2D dimension) {
		Random r = new Random();
		return new Food(new Point2D(r.nextInt((int) dimension.getWidth()), r.nextInt((int) dimension.getHeight())), 50000);
	}

	private static EntityShape generateBody(Individual individual) {
		EntityShape body = individual.getBody();
		body.setColor(Util.FOOD_COLOR);
		return body;
	}

	public Food(Point2D position, double energy) {
		super(EntityShape.foodShape(position), energy);
		this.body.toBack();
	}

	public Food(Individual individual) {
		super(Food.generateBody(individual), 0);
		this.body.toBack();
	}

	@Override
	public void tick(EntityManager em) {
	}

	@Override
	public void onCollide(Entity entity, EntityManager em) {
	}
}
