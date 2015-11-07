package xyz.ll.life.geometry;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Point {
    public double x, y;

    public Point(Point p) {
        set(p);
    }

    public Point(Point p, double dx, double dy) {
        this(p);
        translate(new Point(dx, dy));
    }

    public void set(Point p) {
        this.x = p.x;
        this.y = p.y;
    }

    public void translate(Point vector) {
        this.x += vector.x;
        this.y += vector.y;
    }
}