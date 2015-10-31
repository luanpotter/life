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
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import xyz.ll.life.model.EntityShape;
import xyz.ll.life.model.Individual;

public class Main extends Application {

    private Dimension2D dimension;
    private Game game;
    private Group root;
    private Scene scene;
    private double size;
    private EntityShape preview;
    private EntityShape newPreview;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        dimension = new Dimension2D(600d, 400d);
        root = new Group();
        game = new Game(dimension, root);
        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);
        size = 6d;

        preview = null;
        newPreview = null;

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                try {
                    game.tick(root);
                } catch (Exception e) {

                }
            }

        }.start();

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                if (newPreview != null) {
                    root.getChildren().remove(preview);
                    preview = newPreview;
                    newPreview = null;
                    root.getChildren().add(preview);
                } else if (preview != null) {
                    if (preview.getStrokeWidth() <= 0.1) {
                        root.getChildren().remove(preview);
                        preview = null;
                    } else {
                        preview.setStrokeWidth(preview.getStrokeWidth() - 0.05);
                    }
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

        scene.setOnScroll(e -> {
            this.size += e.getDeltaY() / 20;
            if (this.size < 1) {
                this.size = 1;
            }
            if (this.size > 100) {
                this.size = 100;
            }

            Individual individual = Individual.abiogenesis(new Point2D(e.getX(), e.getY()), this.size);
            individual.getBody().setStrokeWidth(3);
            individual.getBody().setStroke(Color.hsb(0, 0, 1));
            individual.getBody().setColor(Color.TRANSPARENT);
            newPreview = individual.getBody();
        });

        scene.setOnMousePressed(e -> {
            if (preview != null) {
                root.getChildren().remove(preview);
            }
            Individual individual = Individual.abiogenesis(new Point2D(e.getX(), e.getY()), this.size);
            game.add(individual);
        });

        stage.show();
    }
}
