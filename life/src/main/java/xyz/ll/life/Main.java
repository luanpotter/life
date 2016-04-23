package xyz.ll.life;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.MouseButton;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import xyz.ll.life.model.EntityShape;
import xyz.ll.life.model.Individual;
import xyz.ll.life.model.Organic;
import xyz.ll.life.model.world.Dimension;
import xyz.luan.geometry.Point;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Main extends Application {

    private Game game;
    private Canvas canvas;
    private Stage controls, pca;
    private Scene scene;
    private double size;

    private EntityShape preview;
    private EntityShape newPreview;
    private double strokeWidth;

    private static final Map<String, Consumer<Game>> keyBindings = new HashMap();
    static {
        keyBindings.put("q", game -> game.getViewport().zoom(2d));
        keyBindings.put("e", game -> game.getViewport().zoom(.5d));
        keyBindings.put("w", game -> game.getViewport().translate(0, +10));
        keyBindings.put("a", game -> game.getViewport().translate(+10, 0));
        keyBindings.put("s", game -> game.getViewport().translate(0, -10));
        keyBindings.put("d", game -> game.getViewport().translate(-10, 0));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) {
        Dimension dimension = new Dimension(600d, 400d);
        canvas = new Canvas(dimension.getWidth(), dimension.getHeight());
        game = new Game(dimension);
        controls = new Controls(game);
        pca = new GeneticsPCA(game);

        Group root = new Group();
        root.getChildren().add(canvas);
        scene = new Scene(root, dimension.getWidth(), dimension.getHeight(), Color.BLACK);
        size = 6d;

        preview = null;
        newPreview = null;
        new GameLoopWithAT(game, () -> render()).start();

        setupStage(stage);
        controls.show();
        // pca.show();
    }

    private void render() {
        GraphicsContext g = canvas.getGraphicsContext2D();
        game.render(g);
        updatePreview(g);
    }

    private void updatePreview(GraphicsContext g) {
        if (newPreview != null) {
            preview = newPreview;
            newPreview = null;
        } else if (preview != null) {
            if (strokeWidth <= 0.1) {
                preview = null;
            } else {
                strokeWidth -= 0.05;
            }
        }

        double d = g.getLineWidth();
        g.setLineWidth(strokeWidth);

        if (preview != null) {
            preview.draw(g);
        }
        if (newPreview != null) {
            newPreview.draw(g);
        }

        g.setLineWidth(d);
    }

    private void setupStage(Stage stage) {
        stage.setTitle("Game of Life");
        stage.setScene(scene);

        scene.widthProperty().addListener((observableValue, oldSceneWidth, newSceneWidth) -> {
            game.getViewport().newWidth(newSceneWidth.doubleValue());
        });
        scene.heightProperty().addListener((observableValue, oldSceneHeight, newSceneHeight) -> {
            game.getViewport().newHeight(newSceneHeight.doubleValue());
        });

        scene.setOnScroll(e -> {
            this.size += e.getDeltaY() / 20;
            if (this.size < 1) {
                this.size = 1;
            }
            if (this.size > 100) {
                this.size = 100;
            }

            strokeWidth = 3d;
            EntityShape body = Individual.abiogenesis(new Point(e.getX(), e.getY()), this.size).getBody();
            body.setStrokeColor(Color.hsb(0, 0, 1));
            body.setColor(Color.TRANSPARENT);
            newPreview = body;
        });

        scene.setOnMousePressed(e -> {
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                Individual individual = Individual.abiogenesis(new Point(e.getX(), e.getY()), this.size);
                game.add(individual);
            } else if (e.getButton().equals(MouseButton.SECONDARY)) {
                for (Organic organic : game.getEntities()) {
                    if (organic instanceof Individual) {
                        if (organic.getBody().getShape().getBounds().contains(e.getX(), e.getY())) {
                            System.out.println(Arrays.toString(((Individual) organic).getGenome().getValue()));
                            game.setSelected((Individual) organic);
                            return;
                        }
                    }
                }
                game.setSelected(null);
            }
        });

        scene.setOnKeyPressed(e -> {
            String l = e.getText().toLowerCase();
            keyBindings.getOrDefault(l, game -> {}).accept(game);
        });

        stage.setOnCloseRequest(e -> {
            controls.close();
            pca.close();
        });
        stage.show();
    }
}
