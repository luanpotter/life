package de.lighti.clipper;

import java.util.List;
import java.util.stream.Collectors;

import de.lighti.clipper.Clipper.PolyType;
import lombok.experimental.UtilityClass;
import xyz.ll.life.geometry.EmptyShape;
import xyz.ll.life.geometry.MultiShape;
import xyz.ll.life.geometry.OpType;
import xyz.ll.life.geometry.Point;
import xyz.ll.life.geometry.Polygon;
import xyz.ll.life.geometry.Shape;

@UtilityClass
public class PolygonClipperHelper {

    public Shape clip(Polygon thiz, Polygon that, OpType type) {
        Clipper clip = new DefaultClipper();
        clip.addPath(toPath(thiz), PolyType.SUBJECT, true);
        clip.addPath(toPath(that), PolyType.CLIP, true);
        Paths paths = new Paths();
        clip.execute(type, paths);
        if (paths.isEmpty()) {
            return new EmptyShape();
        }
        List<Shape> polys = paths.stream().map(p -> toShape(p)).collect(Collectors.toList());
        if (polys.size() == 1) {
            return polys.get(0);
        }
        return MultiShape.buildNoCheck(polys);
    }

    private Shape toShape(Path p) {
        if (p.isEmpty()) {
            return new EmptyShape();
        }
        return new Polygon(p.toArray(new Point[p.size()]));
    }

    private Path toPath(Polygon polygon) {
        Path path = new Path();
        polygon.getPoints().forEach(p -> path.add(p));
        return path;
    }

}
