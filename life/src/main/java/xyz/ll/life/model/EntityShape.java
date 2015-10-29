package xyz.ll.life.model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class EntityShape extends Polygon {

    private Point2D center;
    private double angle;
    private Color color;
    private Point2D[] points;
    private Point2D velocity;
    private double area;

    public EntityShape(Point2D center) {
        this.center = center;
        this.angle = 0;
        this.area = 0;

        this.setTranslateX(center.getX());
        this.setTranslateY(center.getY());
        this.setRotate(angle);
    }

    public double getArea() {
        return this.area;
    }

    private double calculateArea() {
        double sum = 0;
        for (int i = 0; i < (this.points.length - 1); i++) {
            sum += this.points[i].getX() * this.points[i + 1].getY() -
                    this.points[i].getY() * this.points[i + 1].getX();
        }
        sum += this.points[this.points.length - 1].getX() *
                this.points[0].getY() - this.points[this.points.length - 1].getY() *
                this.points[0].getX();

        return (double) Math.abs(sum) / 2d;
    }

    public void rotate(double angle) {
        this.angle += angle;
        this.setRotate(Math.toDegrees(this.angle));
    }

    public void translate(double x, double y) {
        this.center = new Point2D(this.center.getX() + x, this.center.getY() + y);
        this.setTranslateX(this.center.getX());
        this.setTranslateY(this.center.getY());
    }

    public Point2D[] getVertices() {
        return this.points;
    }

    public void setVertices(Point2D[] points) {
        this.points = points;
        this.getPoints().clear();
        for (int i = 0; i < this.points.length; i++) {
            this.getPoints().addAll(this.points[i].getX(), this.points[i].getY());
        }
        this.area = calculateArea();
    }

    public Point2D getCenter() {
        return this.center;
    }

    public double getAngle() {
        return this.angle;
    }

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
    }

    public Point2D getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Point2D velocity) {
        this.velocity = velocity;
    }

    public void fixPosition(Dimension2D d) {
        while (getCenter().getX() < 0) {
            translate(d.getWidth(), 0);
        }
        while (getCenter().getX() > d.getWidth()) {
            translate(-d.getWidth(), 0);
        }
        while (getCenter().getY() < 0) {
            translate(0, d.getHeight());
        }
        while (getCenter().getY() > d.getHeight()) {
            translate(0, -d.getHeight());
        }
    }

    public void move() {
        translate(getVelocity().getX(), getVelocity().getY());
    }
}
