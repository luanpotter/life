package xyz.ll.life.geometry;

import javafx.scene.canvas.GraphicsContext;

public interface Shape {

    public double area();

    public Rectangle getBounds();

    public void draw(GraphicsContext g);

    public void translate(Point vector);

    public Shape intersection(Shape shape);

    public Shape diff(Shape shape);

    public Shape xor(Shape shape);

    public Shape union(Shape shape);
}
