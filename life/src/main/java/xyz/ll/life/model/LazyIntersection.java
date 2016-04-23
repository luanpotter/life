package xyz.ll.life.model;

import xyz.luan.geometry.Shape;

class LazyIntersection {

    private Entity e1, e2;
    private Shape shape;

    private static final boolean PIXEL_PERFECT_COLLISION = false;

    public LazyIntersection(Entity e1, Entity e2) {
        this.e1 = e1;
        this.e2 = e2;
    }

    public static Shape getShape(Entity e) {
        return PIXEL_PERFECT_COLLISION ? e.getBody().getShape() : e.getBody().getShape().getBounds();
    }

    public Shape getShape() {
        if (shape == null) {
            shape = getShape(e1).intersection(getShape(e2));
        }
        return shape;
    }

    public boolean intersects() {
        return getShape().getBounds().getHeight() > 0 && getShape().getBounds().getWidth() > 0;
    }
}