package xyz.ll.life.geometry;

import java.util.Arrays;
import java.util.List;

import lombok.Data;

@Data
public class Polygon implements Shape {

    private List<Point> points;

    private Polygon(Point... points) {
        this.points = Arrays.asList(points);
    }

    public Rectangle getBounds() {
        double minx = points.stream().mapToDouble(p -> p.getX()).min().getAsDouble();
        double miny = points.stream().mapToDouble(p -> p.getY()).min().getAsDouble();
        double maxx = points.stream().mapToDouble(p -> p.getX()).max().getAsDouble();
        double maxy = points.stream().mapToDouble(p -> p.getY()).max().getAsDouble();

        return new Rectangle(minx, maxx, miny, maxy);
    }

    public Shape intersection(Polygon that) {
        Shape bounds = this.getBounds().intersection(that.getBounds());
        if (bounds.area() == 0) {
            return bounds;
        }
        return null; // TODO << 
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
}
