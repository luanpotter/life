package xyz.ll.life.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Rectangle implements Shape {

    private Point point;
    private double width, height;

    public Rectangle(double startx, double endx, double starty, double endy) {
        this(new Point(startx, starty), endx - startx, endy - starty);
    }

    public double getX() {
        return point.getX();
    }

    public double getY() {
        return point.getY();
    }

    public Shape intersection(Rectangle r) {
        double startx = Math.max(getX(), r.getX());
        double endx = Math.min(getX() + width, r.getX() + r.width);

        double starty = Math.max(getY(), r.getY());
        double endy = Math.min(getY() + height, r.getY() + r.height);

        if (endy < starty || endx < startx || (starty == endy && startx == endx)) {
            return new EmptyShape();
        }

        return new Rectangle(startx, endx, starty, endy);
    }

    @Override
    public double area() {
        return width * height;
    }
}
