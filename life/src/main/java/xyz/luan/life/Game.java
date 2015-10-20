package xyz.luan.life;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import xyz.luan.life.model.Entity;

public class Game {

	private List<Entity> entities;

	public Game() {
		this.entities = new ArrayList<>();
	}

	public void draw(GraphicsContext g) {
		drawBackground(g);
		for (Entity e : entities) {
			e.draw(g);
		}
	}

	private void drawBackground(GraphicsContext g) {
		g.setFill(Color.CYAN);
		g.fillRect(0, 0, 200, 100);
	}
}
