package xyz.ll.life;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import javastat.multivariate.PCA;

public class GeneticsPCA extends Application {

    private double[][] testscores = {
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0},
            {0, 0, 0}
    };

    @Override public void start(Stage stage) {
        stage.setTitle("PCA");
        //defining the axes
        final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        //creating the chart
        final ScatterChart<Number,Number> lineChart =
                new ScatterChart<Number,Number>(xAxis,yAxis);

        lineChart.setTitle("PCA");

        Scene scene  = new Scene(lineChart,800,600);
        lineChart.getData().add(pca());

        stage.setScene(scene);
        stage.show();
    }

    public XYChart.Series pca() {
        XYChart.Series series = new XYChart.Series();
        series.setName("testscores");

        PCA testclass1 = new PCA(1, "covariance", testscores);
        double[][] principalComponents = testclass1.principalComponents;

        for (int i = 0; i < principalComponents[0].length; i++) {
            double a = principalComponents.length > 0 ? principalComponents[0][i] : 0;
            double b = principalComponents.length > 1 ? principalComponents[1][i] : 0;
            System.out.print(a + ",");
            System.out.print(b + " ");
            series.getData().add(new XYChart.Data(a, b));
        }
        System.out.println();

        /*PCA testclass2 = new PCA();
        principalComponents = testclass2.principalComponents(testscores);
        for (int i = 0; i < principalComponents.length; i++) {
            for (int j = 0; j < principalComponents[i].length; j++) {
                System.out.print(principalComponents[i][j] + " ");
            }
            System.out.println();
        }*/

        return series;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
