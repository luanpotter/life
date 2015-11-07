package de.lighti.clipper;

import lombok.experimental.UtilityClass;
import xyz.ll.life.geometry.Point;

@UtilityClass
class PointOperations {

    public static double getDeltaX(Point pt, Point pt2) {
        return Math.abs(pt.x - pt2.x);
    }

    public static double getDeltaY(Point pt, Point pt2) {
        return Math.abs(pt.y - pt2.y);
    }

    public static boolean arePointsClose(Point pt1, Point pt2, double distSqrd) {
        final double dx = pt1.x - pt2.x;
        final double dy = pt1.y - pt2.y;
        return dx * dx + dy * dy <= distSqrd;
    }

    private static double distanceFromLineSqrd(Point pt, Point ln1, Point ln2) {
        // The equation of a line in general form (Ax + By + C = 0)
        // given 2 points (x¹,y¹) & (x²,y²) is ...
        // (y¹ - y²)x + (x² - x¹)y + (y² - y¹)x¹ - (x² - x¹)y¹ = 0
        // A = (y¹ - y²); B = (x² - x¹); C = (y² - y¹)x¹ - (x² - x¹)y¹
        // perpendicular distance of point (x³,y³) = (Ax³ + By³ + C)/Sqrt(A² +
        // B²)
        // see http://en.wikipedia.org/wiki/Perpendicular_distance
        final double A = ln1.y - ln2.y;
        final double B = ln2.x - ln1.x;
        double C = A * ln1.x + B * ln1.y;
        C = A * pt.x + B * pt.y - C;
        return C * C / (A * A + B * B);
    }

    public static boolean isPt2BetweenPt1AndPt3(Point pt1, Point pt2, Point pt3) {
        if (pt1.equals(pt3) || pt1.equals(pt2) || pt3.equals(pt2)) {
            return false;
        } else if (pt1.x != pt3.x) {
            return pt2.x > pt1.x == pt2.x < pt3.x;
        } else {
            return pt2.y > pt1.y == pt2.y < pt3.y;
        }
    }

    public static boolean slopesEqual(Point pt1, Point pt2, Point pt3) {
        return (pt1.y - pt2.y) * (pt2.x - pt3.x) - (pt1.x - pt2.x) * (pt2.y - pt3.y) == 0;
    }

    public static boolean slopesNearCollinear(Point pt1, Point pt2, Point pt3, double distSqrd) {
        // this function is more accurate when the point that's GEOMETRICALLY
        // between the other 2 points is the one that's tested for distance.
        // nb: with 'spikes', either pt1 or pt3 is geometrically between the
        // other pts
        if (Math.abs(pt1.x - pt2.x) > Math.abs(pt1.y - pt2.y)) {
            if (pt1.x > pt2.x == pt1.x < pt3.x) {
                return distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd;
            } else if (pt2.x > pt1.x == pt2.x < pt3.x) {
                return distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd;
            } else {
                return distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd;
            }
        } else {
            if (pt1.y > pt2.y == pt1.y < pt3.y) {
                return distanceFromLineSqrd(pt1, pt2, pt3) < distSqrd;
            } else if (pt2.y > pt1.y == pt2.y < pt3.y) {
                return distanceFromLineSqrd(pt2, pt1, pt3) < distSqrd;
            } else {
                return distanceFromLineSqrd(pt3, pt1, pt2) < distSqrd;
            }
        }
    }
}
