package xyz.luan.life;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.stage.Stage;
import xyz.luan.life.model.EntityShape;

import java.util.Random;


public class Main extends Application {

	private Game game;
    private Group root;
    private Scene scene;

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
        Dimension2D dimension = new Dimension2D(400, 400);
        game = new Game(dimension);
        root = new Group();
        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);

        setupStage(stage);

        double[] chars = {10, 1, 3, 3, 3, 10, 1, 1, 1, 1, 1, 1, 1, 1};
        EntityShape entityShape = new EntityShape(new Point2D(100, 100), chars, Color.RED);
        root.getChildren().add(entityShape);
        for (Point2D p : entityShape.getPoints2D()) {
            System.out.print("<" + p.getX() + "," + p.getY() + ">");
        }
        System.out.println();
        System.out.println("Area: " + entityShape.estimateArea());
        System.out.println("Arc: " + entityShape.ARC);

        /*Platform.runLater(() -> {
            for (int i = 0; i < 10; i++) {
                game.tick(root);
            }
        });*/
    }

	private void setupStage(Stage stage) {
		stage.setTitle("Game of Life");
		stage.setScene(scene);
		stage.show();
	}
}
