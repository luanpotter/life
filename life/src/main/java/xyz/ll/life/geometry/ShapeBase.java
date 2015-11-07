package xyz.ll.life.geometry;

public abstract class ShapeBase implements Shape {

    protected abstract Shape op(Shape shape, OpType type);

    public Shape intersection(Shape shape) {
        return op(shape, OpType.INTERSECTION);
    }

    public Shape diff(Shape shape) {
        return op(shape, OpType.DIFFERENCE);
    }

    public Shape xor(Shape shape) {
        return op(shape, OpType.XOR);
    }

    public Shape union(Shape shape) {
        return op(shape, OpType.UNION);
    }
}
