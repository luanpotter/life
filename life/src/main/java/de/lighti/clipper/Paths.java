package de.lighti.clipper;

import java.util.ArrayList;

/**
 * A pure convenience class to avoid writing List<Path> everywhere.
 *
 * @author Tobias Mahlmann
 *
 */
class Paths extends ArrayList<Path> {

    public static Paths closedPathsFromPolyTree(PolyTree polytree) {
        final Paths result = new Paths();
        // result.Capacity = polytree.Total;
        result.addPolyNode(polytree, PolyNode.NodeType.CLOSED);
        return result;
    }

    public static Paths makePolyTreeToPaths(PolyTree polytree) {

        final Paths result = new Paths();
        // result.Capacity = polytree.Total;
        result.addPolyNode(polytree, PolyNode.NodeType.ANY);
        return result;
    }

    public static Paths openPathsFromPolyTree(PolyTree polytree) {
        final Paths result = new Paths();
        // result.Capacity = polytree.ChildCount;
        for (final PolyNode c : polytree.getChilds()) {
            if (c.isOpen()) {
                result.add(c.getPolygon());
            }
        }
        return result;
    }

    /**
     *
     */
    private static final long serialVersionUID = 1910552127810480852L;

    public Paths() {
        super();
    }

    public Paths(int initialCapacity) {
        super(initialCapacity);
    }

    public void addPolyNode(PolyNode polynode, PolyNode.NodeType nt) {
        boolean match = true;
        switch (nt) {
        case OPEN:
            return;
        case CLOSED:
            match = !polynode.isOpen();
            break;
        default:
            break;
        }

        if (polynode.getPolygon().size() > 0 && match) {
            add(polynode.getPolygon());
        }
        for (final PolyNode pn : polynode.getChilds()) {
            addPolyNode(pn, nt);
        }
    }

    public Paths cleanPolygons() {
        return cleanPolygons(1.415);
    }

    public Paths cleanPolygons(double distance) {
        final Paths result = new Paths(size());
        for (int i = 0; i < size(); i++) {
            result.add(get(i).cleanPolygon(distance));
        }
        return result;
    }

    public void reversePaths() {
        for (final Path poly : this) {
            poly.reverse();
        }
    }

}
