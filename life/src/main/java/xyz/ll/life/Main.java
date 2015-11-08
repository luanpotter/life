package xyz.ll.life;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.EntityShape;
import xyz.ll.life.model.Individual;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Polygon;
import xyz.luan.geometry.Rectangle;
import xyz.luan.geometry.Shape;

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

    public static void runTest(GraphicsContext g) {
        Shape square = new Rectangle(new Point(100, 100), 100, 100);
        Shape triangle = new Polygon(new Point(150, 150), new Point(150, 250), new Point(220, 130));
        g.setFill(Color.MAGENTA);
        square.draw(g);
        g.setFill(Color.CYAN);
        triangle.draw(g);
        g.setFill(Color.BLUEVIOLET);
        square.intersection(triangle).draw(g);

        square.translate(new Point(300, 0));
        triangle.translate(new Point(300, 0));
        g.setFill(Color.MAGENTA);
        square.draw(g);
        g.setFill(Color.CYAN);
        triangle.draw(g);
        g.setFill(Color.BLUEVIOLET);
        square.union(triangle).draw(g);

        square.translate(new Point(0, 150));
        triangle.translate(new Point(0, 150));
        g.setFill(Color.MAGENTA);
        square.draw(g);
        g.setFill(Color.CYAN);
        triangle.draw(g);
        g.setFill(Color.BLUEVIOLET);
        square.xor(triangle).draw(g);

        square.translate(new Point(-300, 0));
        triangle.translate(new Point(-300, 0));
        g.setFill(Color.BLUEVIOLET);
        square.diff(triangle).draw(g);

        double a1 = square.union(triangle).area();
        double a2 = square.intersection(triangle).area() + square.xor(triangle).area();
        System.out.println(a1 - a2);
    }

    @Override
    public void start(Stage stage) {
        dimension = new Dimension2D(600d, 400d);
        root = new Group();
        game = new Game(dimension, root);
        controls = new Controls(game);

        Canvas canvas = new Canvas(600d, 400d);
        runTest(canvas.getGraphicsContext2D());
        Group root2 = new Group();
        root2.getChildren().add(canvas);
        scene = new Scene(root2, dimension.getWidth(), dimension.getHeight(), Color.BLACK);
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
        // controls.show();
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
