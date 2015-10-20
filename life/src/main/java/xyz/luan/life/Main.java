package xyz.luan.life;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.stage.Stage;

public class Main extends Application {

	private Game game;
	private Canvas canvas;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		canvas = new Canvas(300, 250);
		game = new Game();

		setupStage(stage);

		draw();
	}

	private void setupStage(Stage stage) {
		stage.setTitle("Hello World!");
		stage.setScene(new Scene(rootPane()));
		stage.show();
	}

	private Group rootPane() {
		Group root = new Group();
		root.getChildren().add(canvas);
		return root;
	}

	public void draw() {
		game.draw(canvas.getGraphicsContext2D());
	}
}
