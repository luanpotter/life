package xyz.ll.life.model;

import xyz.luan.geometry.Shape;

class LazyIntersection {

    private Entity e1, e2;
    private Shape shape;

    public LazyIntersection(Entity e1, Entity e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public Shape getShape() {
        if (shape == null) {
            shape = e1.intersection(e2);
        }
        return shape;
    }

    public boolean intersects() {
        return getShape().getBounds().getHeight() > 0 && getShape().getBounds().getWidth() > 0;
    }
}