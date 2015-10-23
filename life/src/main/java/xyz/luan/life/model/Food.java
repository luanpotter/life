package xyz.luan.life.model;

import javafx.scene.Group;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;

import java.awt.geom.Point2D;


public class Food extends Entity {

    private static final double SIZE = 2;

    private static Polygon generateBody(Point2D position) {
        Polygon body = new Polygon(position.getX() - SIZE,
                position.getY() - SIZE,
                position.getX() + SIZE,
                position.getY() - SIZE,
                position.getX() + SIZE,
                position.getY() + SIZE,
                position.getX() - SIZE,
                position.getY() + SIZE);
        return body;
    }

    public Food(Point2D position, double energy) {
        super(Food.generateBody(position), energy);
    }

    @Override
    public void tick() {

    }

    @Override
    public void onCollide(Entity entity, Shape intersection, Group group) {

    }
}
