package xyz.ll.life.lab;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;

public class Pca3dDialog extends AbstractAnalysis {
    private Coord3d[] points;
    private Color[] colors;
    private LineStrip[] lineStrips;

    public Pca3dDialog(Coord3d[] points, Color[] colors, LineStrip[] lineStrips) {
        this.points = points;
        this.colors = colors;
        this.lineStrips = lineStrips;
    }

    public void init(){
        Scatter scatter = new Scatter(points, colors);
        scatter.setWidth(3);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);

        for (LineStrip lineStrip : lineStrips) {
            chart.getScene().getGraph().add(lineStrip);
        }

        chart.getScene().getGraph().add(new Sphere(new Coord3d(0, 0, 0), 0.1f, 10, Color.BLACK));
    }
}