package xyz.ll.life.model;

import java.util.List;
import java.util.stream.Collectors;

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

    public EntityShape(Shape shape) {
        this.shape = shape;
        this.area = shape.area();
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

    public Color getColor() {
        return color;
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

    public double getAngleAcc() {
        return angleAcc;
    }

    public void setAngleAcc(double angleAcc) {
        this.angleAcc = angleAcc;
    }

    public void move() {
        translate(getVelocity().getX(), getVelocity().getY());
    }

    public void unmove() {
        translate(-getVelocity().getX(), -getVelocity().getY());
    }

    public double getAngle() {
        return new Point(0, 1).angle(velocity);
    }

    public Shape getShape() {
        return shape;
    }

    public Point getCenter() {
        return this.shape.getBounds().getCenter();
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

    public void setPosition(Point point) {
        this.shape.translate(point.minusTo(getCenter()));
    }
}
