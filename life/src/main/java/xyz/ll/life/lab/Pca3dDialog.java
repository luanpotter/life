package xyz.ll.life.lab;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.primitives.Sphere;
import org.jzy3d.plot3d.rendering.canvas.Quality;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Pca3dDialog extends AbstractAnalysis {
    private Coord3d[] points;
    private Color[] colors;
    private LineStrip[] lineStrips;

    private Scatter scatter;
    public ArrayList<Coord3d> pAux;
    public ArrayList<Color> cAux;

    public Pca3dDialog(Coord3d[] points, Color[] colors, LineStrip[] lineStrips) {
        this.points = points;
        this.colors = colors;
        this.lineStrips = lineStrips;
        pAux = new ArrayList<>(Arrays.asList(points));
        cAux = new ArrayList<>(Arrays.asList(colors));
    }

    public void addPoints(Coord3d point) {
        pAux.add(point);
        scatter.setData(pAux.toArray(new Coord3d[pAux.size()]));
    }

    public void addColors(Color color) {
        cAux.add(color);
        scatter.setColors(cAux.toArray(new Color[cAux.size()]));
    }

    public void addLine(LineStrip lineStrip) {
        chart.getScene().getGraph().add(lineStrip);
    }

    public void init(){
        scatter = new Scatter(points, colors);
        scatter.setWidth(3);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);

        if (lineStrips != null) {
            for (LineStrip lineStrip : lineStrips) {
                chart.getScene().getGraph().add(lineStrip);
            }
        }

        //chart.getScene().getGraph().add(new Sphere(new Coord3d(0, 0, 0), 0.1f, 10, Color.BLACK));
    }
}