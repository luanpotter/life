package xyz.ll.life.model;

import java.util.List;
import java.util.stream.Collectors;

import javafx.geometry.Dimension2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import xyz.luan.geometry.Point;
import xyz.luan.geometry.Polygon;
import xyz.luan.geometry.Shape;

public class EntityShape {

    private Shape shape;
    private Color color;
    private Color strokeColor;
    private double area;

    private Point velocity;
    private double angleAcc;

    public EntityShape(Point center, List<Point> vertices) {
        List<Point> points = vertices.stream().map(v -> v.translateTo(center)).collect(Collectors.toList());
        this.shape = new Polygon(points);
        this.area = this.shape.area();
    }

    public double getArea() {
        return this.area;
    }

    public void translate(double x, double y) {
        shape.translate(new Point(x, y));
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Color getStrokeColor() {
        return strokeColor;
    }

    public void setStrokeColor(Color strokeColor) {
        this.strokeColor = strokeColor;
    }

    public Point getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Point velocity) {
        this.velocity = velocity;
    }

    public void fixPosition(Dimension2D d) {
        while (shape.getBounds().getX() + shape.getBounds().getWidth() < 0) {
            translate(d.getWidth(), 0);
        }
        while (shape.getBounds().getX() > d.getWidth()) {
            translate(-d.getWidth(), 0);
        }
        while (shape.getBounds().getY() + shape.getBounds().getHeight() < 0) {
            translate(0, d.getHeight());
        }
        while (shape.getBounds().getY() > d.getHeight()) {
            translate(0, -d.getHeight());
        }
    }

    public double getAngleAcc() {
        return angleAcc;
    }

    public void setAngleAcc(double angleAcc) {
        this.angleAcc = angleAcc;
    }

    public void move() {
        translate(getVelocity().getX(), getVelocity().getY());
    }

    public double getAngle() {
        return new Point(0, 1).angle(velocity);
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
        this.area = shape.area();
    }

    public void draw(GraphicsContext g) {
        g.setFill(color);
        shape.fill(g);
        if (strokeColor != null) {
            g.setStroke(strokeColor);
            shape.draw(g);
        }
    }
}
