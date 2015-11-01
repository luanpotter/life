package xyz.ll.life;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.EntityShape;
import xyz.ll.life.model.Individual;

public class Main extends Application {

    private Dimension2D dimension;
    private Game game;
    private Group root;
    private Controls controls;
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
        controls = new Controls(game);

        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);
        size = 6d;

        preview = null;
        newPreview = null;

        new AnimationTimer() {

            @Override
            public void handle(long now) {
                game.tick();
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
        controls.show();
    }

    private void setupStage(Stage stage) {
        stage.setTitle("Game of Life");
        stage.setScene(scene);

        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            // DANGER//
            dimension = new Dimension2D(newSceneWidth.doubleValue(), dimension.getHeight());
            game.setDimension(dimension);
        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            // DANGER//
            dimension = new Dimension2D(dimension.getWidth(), newSceneHeight.doubleValue());
            game.setDimension(dimension);
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
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (preview != null) {
                    root.getChildren().remove(preview);
                }
                Individual individual = Individual.abiogenesis(new Point2D(e.getX(), e.getY()), this.size);
                game.add(individual);
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                for (Entity entity : game.getEntities()) {
                    if (entity instanceof Individual) {
                        if (entity.getBody().getBoundsInParent().contains(e.getX(), e.getY())) {
                            game.setSelected((Individual) entity);
                            return;
                        }
                    }
                }
                game.setSelected(null);
            }
        });

        stage.setOnCloseRequest(e -> controls.close());
        stage.show();
    }
}
