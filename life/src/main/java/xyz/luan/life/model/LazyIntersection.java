package xyz.luan.life.model;

import javafx.scene.shape.Shape;

class LazyIntersection {

    private Entity e1, e2;
    private Shape shape;

    public LazyIntersection(Entity e1, Entity e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Shape getShape() {
        if (shape == null) {
            shape = e1.intersects(e2);
        }
        return shape;
    }

    public boolean intersects() {
        return getShape() != null && getShape().getLayoutBounds().getHeight() > 0 && getShape().getLayoutBounds().getWidth() > 0;
    }
}