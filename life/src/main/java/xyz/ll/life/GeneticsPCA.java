package xyz.ll.life;

import java.util.List;
import java.util.stream.Collectors;

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import xyz.ll.life.model.Individual;

public class GeneticsPCA extends Stage {

    private Game game;

    public GeneticsPCA(Game game) {
        setTitle("Game of Life - PCA");
        setScene(new Scene(content(), 300, 400));
        this.game = game;
    }

    private Pane content() {
        BorderPane grid = new BorderPane();
        grid.setCenter(chart(2000));
        return grid;
    }

    private ScatterChart<Number, Number> chart(int time) {
        // defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time (" + (((float) time) / 1000f) + "s)");
        // creating the chart
        final ScatterChart<Number, Number> lineChart = new ScatterChart<Number, Number>(xAxis, yAxis);

        lineChart.setTitle("PCA");

        XYChart.Series<Number, Number> series = new XYChart.Series<>();
        series.setName("PCS");
        lineChart.getData().add(series);

        Thread t = new Thread() {

            @Override
            public void run() {
                while (true) {
                    try {
                        if (game != null) {
                            Platform.runLater(() -> {
                                pca(game.individuals().collect(Collectors.toList()), series);
                            });
                        }
                        Thread.sleep(time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();

        lineChart.setPrefSize(300, 340);
        return lineChart;
    }

    private void pca(List<Individual> individuals, XYChart.Series<Number, Number> series) {
        double[][] testscores = new double[individuals.size()][individuals.size()];
        for (int i = 0; i < individuals.size(); i++) {
            for (int j = 0; j < individuals.size(); j++) {
                testscores[i][j] = Math
                        .abs(individuals.get(i).getGenome().geneticDistance(individuals.get(j).getGenome()));
            }
        }
        // System.out.println(Arrays.deepToString(testscores));

        double[][] principalComponents = null;

        series.getData().clear();
        for (int i = 0; i < principalComponents[0].length; i++) {
            double a = principalComponents.length > 0 ? principalComponents[0][i] : 0;
            double b = principalComponents.length > 1 ? principalComponents[1][i] : 0;
            series.getData().add(new XYChart.Data<>(a, b));
        }
    }
}