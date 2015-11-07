package xyz.ll.life.geometry;

import java.util.Arrays;
import java.util.List;

import de.lighti.clipper.PolygonClipperHelper;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class Polygon extends ShapeBase {

    private List<Point> points;

    public Polygon(Point... points) {
        this.points = Arrays.asList(points);
    }

    public Rectangle getBounds() {
        double minx = points.stream().mapToDouble(p -> p.getX()).min().getAsDouble();
        double miny = points.stream().mapToDouble(p -> p.getY()).min().getAsDouble();
        double maxx = points.stream().mapToDouble(p -> p.getX()).max().getAsDouble();
        double maxy = points.stream().mapToDouble(p -> p.getY()).max().getAsDouble();

        return new Rectangle(minx, maxx, miny, maxy);
    }

    @Override
    public Shape op(Shape that, OpType type) {
        if (that instanceof Polygon) {
            return op((Polygon) that, type);
        }
        if (that instanceof Rectangle) {
            return op(((Rectangle) that).toPolygon(), type);
        }
        if (that instanceof EmptyShape || that instanceof MultiShape) {
            return ((ShapeBase) that).op(this, type);
        }
        throw new RuntimeException("Unknown shape type...");
    }

    private Shape op(Polygon that, OpType type) {
        return PolygonClipperHelper.clip(this, that, type);
    }

    @Override
    public double area() {
        double sum = 0;
        int last = points.size() - 1;
        for (int i = 0; i < last; i++) {
            sum += points.get(i).getX() * points.get(i + 1).getY() - points.get(i).getY() * points.get(i + 1).getX();
        }
        sum += points.get(last).getX() * points.get(0).getY() - points.get(last).getY() * points.get(0).getX();

        return Math.abs(sum) / 2d;
    }

    @Override
    public void draw(GraphicsContext g) {
        double[] xs = points.stream().mapToDouble(p -> p.getX()).toArray();
        double[] ys = points.stream().mapToDouble(p -> p.getY()).toArray();
        g.fillPolygon(xs, ys, points.size());
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

        //

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

        System.out.println(
                square.union(triangle).area() - (square.intersection(triangle).area() + square.xor(triangle).area()));
    }

    @Override
    public void translate(Point vector) {
        points.forEach(p -> p.translate(vector));
    }
}
