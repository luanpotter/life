package xyz.ll.life;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javastat.multivariate.PCA;
import xyz.ll.life.model.Individual;

public class GeneticsPCA extends Stage {

    private static final Background DARK_BG = new Background(
            new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY));

    private Game game;

    public GeneticsPCA(Game game) {
        setTitle("Game of Life - PCA");
        setScene(new Scene(content(), 300, 400));
        this.game = game;
    }

    private Pane content() {
        BorderPane grid = new BorderPane();

        grid.setCenter(chart(1000));

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

        XYChart.Series series = new XYChart.Series();
        series.setName("PCS");
        lineChart.getData().add(series);

        new Thread() {

            private int x = 0;

            @Override
            public void run() {
                while (true) {
                    try {
                        if (game != null) {
                            series.getData().clear();
                            pca(game.getIndividuals(), series);
                        }
                        Thread.sleep(time);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

        lineChart.setPrefSize(300, 340);
        return lineChart;
    }

    private static void pca(Individual[] individuals, XYChart.Series series) {
        double[][] testscores = new double[individuals.length][individuals.length];
        for (int i = 0; i < individuals.length; i++) {
            for (int j = 0; j < individuals.length; j++) {
                testscores[i][j] = individuals[i].getGenome().geneticDistance(individuals[j].getGenome());
                // System.out.print(testscores[i][j] + " ");
            }
            // System.out.println();
        }
        // System.out.println();

        PCA testclass1 = new PCA(1, "covariance", testscores);
        double[][] principalComponents = testclass1.principalComponents;

        for (int i = 0; i < principalComponents[0].length; i++) {
            double a = principalComponents.length > 0 ? principalComponents[0][i] : 0;
            double b = principalComponents.length > 1 ? principalComponents[1][i] : 0;
            // System.out.print(a + ",");
            // System.out.print(b + " ");
            series.getData().add(new XYChart.Data(a, b));
        }
        // System.out.println();
    }

    private Text title() {
        Text scenetitle = new Text("PCA Panel");
        scenetitle.setFont(Font.font("Tahoma", FontWeight.NORMAL, 20));
        return scenetitle;
    }
}