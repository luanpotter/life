package xyz.ll.life;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.ll.life.model.Individual;

public class Main extends Application {

    private Game game;
    private Group root;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Dimension2D dimension = new Dimension2D(600, 400);
        root = new Group();
        game = new Game(dimension, root);
        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);

        setupStage(stage);

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                try {
                    game.tick(root);
                } catch (Exception e) {

                }
            }

        }.start();

        scene.setOnMousePressed(e -> {
            System.out.println(".");
            Individual individual = Individual.abiogenesis(new Point2D(e.getX(), e.getY()));
            game.add(individual);
        });
    }

    private void setupStage(Stage stage) {
        stage.setTitle("Game of Life");
        stage.setScene(scene);
        stage.show();
    }
}
