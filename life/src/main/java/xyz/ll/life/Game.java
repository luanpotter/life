package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.Food;
import xyz.ll.life.model.Individual;

public class Game {

	private Group root;
	private List<Entity> entities;
	private Dimension2D dimension;
	private static final Random rand = new Random();

	public Game(Dimension2D dimension, Group root) {
		this.entities = new ArrayList<>();
		this.root = root;
		this.dimension = dimension;

		randomStart(dimension);
	}

	private void randomStart(Dimension2D dimension) {
        for (int i = 0; i < 100; i++) {
            add(Food.randomFood(dimension));
		}
        for (int i = 0; i < 10; i++) {
            add(Individual.abiogenesis(dimension));
        }
	}

	public void tick(Group group) throws Exception {
		generateRandomFood();

		EntityManager em = new EntityManager();
		for (Entity e : entities) {
			if (!em.alive(e)) {
				continue;
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
		if (rand.nextInt(5) == 0) {
			add(Food.randomFood(dimension));
		}
	}

	private void remove(Entity e) {
		entities.remove(e);
		root.getChildren().remove(e.getBody());
	}

	private void add(Entity e) {
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
}
