package xyz.ll.life.model;

import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class EntityShape extends Polygon {

    private Point2D center;
    private Color color;
    private Color ColorStroke;
    private Point2D[] points;
    private double area;

    private Point2D velocity;
    private double angleAcc;

    public EntityShape(Point2D center) {
        this.center = center;
        this.area = 0;

        this.setTranslateX(center.getX());
        this.setTranslateY(center.getY());
    }

    public double getArea() {
        return this.area;
    }

    private double calculateArea() {
        double sum = 0;
        for (int i = 0; i < (this.points.length - 1); i++) {
            sum += this.points[i].getX() * this.points[i + 1].getY()
                    - this.points[i].getY() * this.points[i + 1].getX();
        }
        sum += this.points[this.points.length - 1].getX() * this.points[0].getY()
                - this.points[this.points.length - 1].getY() * this.points[0].getX();

        return (double) Math.abs(sum) / 2d;
    }

    public void translate(double x, double y) {
        // DANGER//
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

    public Color getColor() {
        return this.color;
    }

    public void setColor(Color color) {
        this.color = color;
        this.setFill(color);
    }

    public Color getColorStroke() {
        return this.ColorStroke;
    }

    public void setColorStroke(Color colorStroke) {
        this.ColorStroke = colorStroke;
        this.setStroke(this.ColorStroke);
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

    public double getAngleAcc() {
        return angleAcc;
    }

    public void setAngleAcc(double angleAcc) {
        this.angleAcc = angleAcc;
    }

    public void move() {
        translate(getVelocity().getX(), getVelocity().getY());
        rotate();
    }

    private void rotate() {
        Point2D defaultDirection = this.center.add(0d, 1d);
        this.setRotate(defaultDirection.angle(velocity));
    }
}
