package xyz.ll.life.pca;

import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;


/**
 * Created by lucas-cleto on 2/17/16.
 */
public class PhylogeneticsTreeAnalyser extends AbstractAnalysis {

    private Coord3d[] points;
    private Color[] colors;
    private LineStrip[] lineStrips;

    public PhylogeneticsTreeAnalyser(Coord3d[] points, Color[] colors, LineStrip[] lineStrips) {
        this.points = points;
        this.colors = colors;
        this.lineStrips = lineStrips;
    }

    public void init(){
        Scatter scatter = new Scatter(points, colors);
        scatter.setWidth(3);
        chart = AWTChartComponentFactory.chart(Quality.Advanced, "newt");
        chart.getScene().add(scatter);

        if (lineStrips != null) {
            for (LineStrip lineStrip : lineStrips) {
                chart.getScene().getGraph().add(lineStrip);
            }
        }
    }
}
