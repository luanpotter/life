package xyz.luan.life;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Dimension2D;
import javafx.scene.Group;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import xyz.luan.life.model.Entity;
import xyz.luan.life.model.Individual;

public class Game {

	private List<Entity> entities;
    private Dimension2D dimension;

	public Game(Dimension2D dimension) {
		this.entities = new ArrayList<Entity>();
        this.dimension = dimension;

        entities.add(new Individual());
	}

    public void tick(Group group) {
        for (Entity entity : entities) {
            entity.tick();
        }
    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Dimension2D getDimension() {
        return dimension;
    }
}
