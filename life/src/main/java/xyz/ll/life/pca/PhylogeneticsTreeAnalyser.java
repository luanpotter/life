package xyz.ll.life.pca;

import javafx.geometry.Point2D;
import javafx.geometry.Point3D;
import org.jzy3d.analysis.AbstractAnalysis;
import org.jzy3d.chart.factories.AWTChartComponentFactory;
import org.jzy3d.colors.Color;
import org.jzy3d.maths.Coord3d;
import org.jzy3d.plot3d.primitives.LineStrip;
import org.jzy3d.plot3d.primitives.Point;
import org.jzy3d.plot3d.primitives.Scatter;
import org.jzy3d.plot3d.rendering.canvas.Quality;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class PhylogeneticsTreeAnalyser extends AbstractAnalysis {

    private Coord3d[] points;
    private Color[] colors;
    private LineStrip[] lineStrips;

    public PhylogeneticsTreeAnalyser(PhylogeneticsTree phylogeneticsTree) {
        this.points = new Coord3d[phylogeneticsTree.getPoints().length];
        this.colors = new Color[phylogeneticsTree.getColors().length];
        this.lineStrips = new LineStrip[phylogeneticsTree.getConnections().length];

        int i = 0;
        for (Point3D p : phylogeneticsTree.getPoints()) {
            points[i++] = new Coord3d(p.getX(), p.getY(), p.getZ());
        }

        int maxColor = phylogeneticsTree.countColors();
        i = 0;
        for (Integer c : phylogeneticsTree.getColors()) {
            float hue = maxColor == 0 ? 0.5f : (float) c / maxColor;
            java.awt.Color color = java.awt.Color.getHSBColor(hue, 1f, 1f);
            colors[i++] = new Color(color.getRed(), color.getGreen(), color.getBlue());
        }

        i = 0;
        for (Point2D l : phylogeneticsTree.getConnections()) {
            lineStrips[i++] = new LineStrip(new Point(points[(int) l.getX()], colors[(int) l.getX()]),
                    new Point(points[(int) l.getY()], colors[(int) l.getY()]));
        }
    }

    public void init() {
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
