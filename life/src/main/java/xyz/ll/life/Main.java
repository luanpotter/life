package xyz.ll.life;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.ll.life.model.Individual;

public class Main extends Application {

    private Dimension2D dimension;
    private Game game;
    private Group root;
    private Scene scene;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        dimension = new Dimension2D(600, 400);
        root = new Group();
        game = new Game(dimension, root);
        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                try {
                    game.tick(root);
                } catch (Exception e) {

                }
            }

        }.start();

        setupStage(stage);
    }

    private void setupStage(Stage stage) {
        stage.setTitle("Game of Life");
        stage.setScene(scene);

        scene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneWidth, Number newSceneWidth) {
                //DANGER//
                dimension = new Dimension2D(newSceneWidth.doubleValue(), dimension.getHeight());
                game.setDimension(dimension);
            }
        });
        scene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observableValue, Number oldSceneHeight, Number newSceneHeight) {
                //DANGER//
                dimension = new Dimension2D(dimension.getWidth(), newSceneHeight.doubleValue());
                game.setDimension(dimension);
            }
        });

        scene.setOnMousePressed(e -> {
            System.out.println(".");
            Individual individual = Individual.abiogenesis(new Point2D(e.getX(), e.getY()), 8d);
            game.add(individual);
        });

        stage.show();
    }
}
