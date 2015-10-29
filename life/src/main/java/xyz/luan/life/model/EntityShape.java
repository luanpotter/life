package xyz.luan.life.model;

import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import xyz.luan.life.model.genetics.Genome;
import xyz.luan.life.model.genetics.MorfologicGene;

public class EntityShape extends Polygon {

    private Point2D center;
    private double angle;
    private Color color;
    private Point2D[] points;
    private Point2D velocity;

    public EntityShape(Point2D center) {
        this.center = center;
        this.angle = 0;

        this.setTranslateX(center.getX());
        this.setTranslateY(center.getY());
        this.setRotate(angle);    }

    public double estimateArea() {
        double sum = 0;
        for (int i = 0; i < (points.length - 1); i++) {
            sum += points[i].getX() * points[i + 1].getY() - points[i].getY() * points[i + 1].getX();
        }
        sum += points[points.length - 1].getX() * points[0].getY() - points[points.length - 1].getY() * points[0].getX();

        return (double) Math.abs(sum) / 2d;
    }

    public void setPoints(Point2D[] points) {
        this.points = points;
        this.getPoints().clear();
        for (int i = 0; i < this.points.length; i++) {
            this.getPoints().addAll(this.points[i].getX(), this.points[i].getY());
        }
    }

    public Point2D getCenter() {
        return center;
    }

    public double getAngle() {
        return angle;
    }

    public void rotate(double angle) {
        this.angle += angle;
        this.setRotate(Math.toDegrees(this.angle));
    }

    public void translate(double x, double y) {
        center = new Point2D(center.getX() + x, center.getY() + y);
        this.setTranslateX(center.getX());
        this.setTranslateY(center.getY());
    }
    
    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.setFill(color);
    }

    public Point2D[] getPoints2D() {
        return points;
    }

    public Point2D getVelocity() {
        return velocity;
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
