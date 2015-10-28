package xyz.luan.life.model;

import javafx.event.EventHandler;
import javafx.geometry.Dimension2D;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;

public class EntityShape extends Polygon {

	private double arc;
	private int precision;
	private Point2D center;
	private double angle;
	private double[] characteristics;
	private Point2D[] points;
	private Point2D velocity;

	public EntityShape(Point2D center, double[] characteristics, int precision) {
		this.center = center;
		this.characteristics = characteristics;
		this.angle = 0;
		this.precision = precision;
		this.arc = 2 * Math.PI / (double) precision;

		generatePoints();
		this.setTranslateX(center.getX());
		this.setTranslateY(center.getY());
		this.setRotate(angle);

		this.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent t) {
				for (double d : characteristics) {
					System.out.printf("%06.3f ", d);
				}
				System.out.println();
			}
		});
	}

	private Point2D getPoint(double t) {
		int i = 0;
		double a = characteristics[i++] * Math.pow(Math.sin(characteristics[i++] * 0 + t), 2);
		double b = characteristics[i++] * Math.pow(Math.sin(characteristics[i++] * t), 2) * Math.pow(Math.cos(characteristics[i++] * t), 2);
		double c = characteristics[i++] * Math.pow(Math.cos(characteristics[i++] * 0 + t), 2);
		double d = characteristics[i++] * Math.sin(characteristics[i++] * t);
		double e = characteristics[i++] * Math.sin(characteristics[i++] * t) * Math.cos(characteristics[i++] * t);
		double f = characteristics[i++] * Math.cos(characteristics[i++] * t);
		double radius = a + b + c + d + e + f;
		return new Point2D(radius * Math.cos(t), radius * Math.sin(t));
	}

	private void generatePoints() {
		points = new Point2D[precision];
		for (int i = 0; i < precision; i++) {
			Point2D point = getPoint(i * arc);
			points[i] = point;
			this.getPoints().addAll(point.getX(), point.getY());
		}
	}

	public double estimateArea() {
		double sum = 0;
		for (int i = 0; i < (precision - 1); i++) {
			sum += points[i].getX() * points[i + 1].getY() - points[i].getY() * points[i + 1].getX();
		}
		sum += points[precision - 1].getX() * points[0].getY() - points[precision - 1].getY() * points[0].getX();

		return (double) Math.abs(sum) / 2d;
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

	public double[] getCharacteristics() {
		return characteristics;
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

	public void move() {
		translate(getVelocity().getX(), getVelocity().getY());
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
}
