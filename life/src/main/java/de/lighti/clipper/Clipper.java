package de.lighti.clipper;

import xyz.ll.life.geometry.OpType;

interface Clipper {
    enum Direction {
        RIGHT_TO_LEFT, LEFT_TO_RIGHT
    };

    public enum EndType {
        CLOSED_POLYGON, CLOSED_LINE, OPEN_BUTT, OPEN_SQUARE, OPEN_ROUND
    };

    public enum JoinType {
        SQUARE, ROUND, MITER
    };

    public enum PolyFillType {
        EVEN_ODD, NON_ZERO, POSITIVE, NEGATIVE
    };

    public enum PolyType {
        SUBJECT, CLIP
    };

    // InitOptions that can be passed to the constructor ...
    public final static int REVERSE_SOLUTION = 1;

    public final static int STRICTLY_SIMPLE = 2;

    public final static int PRESERVE_COLINEAR = 4;

    boolean addPath(Path pg, PolyType polyType, boolean Closed);

    boolean addPaths(Paths ppg, PolyType polyType, boolean closed);

    void clear();

    boolean execute(OpType clipType, Paths solution);

    boolean execute(OpType clipType, Paths solution, PolyFillType subjFillType, PolyFillType clipFillType);

    boolean execute(OpType clipType, PolyTree polytree);

    public boolean execute(OpType clipType, PolyTree polytree, PolyFillType subjFillType, PolyFillType clipFillType);
}
