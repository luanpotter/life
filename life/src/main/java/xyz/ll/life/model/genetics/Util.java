package xyz.ll.life.model.genetics;

import javafx.geometry.Point2D;
import javafx.scene.transform.Rotate;

public class Util {

    public static double random(double d1, double d2) {
        return Math.min(d1, d2) + Math.random() * Math.abs(d1 - d2);
    }

    public static double positive(double d) {
        if (d > 0) {
            return d;
        } else {
            return 0;
        }
    }
}
