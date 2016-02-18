package xyz.ll.life.pca;

import javafx.geometry.Point2D;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by lucas-cleto on 2/16/16.
 */
public class Snapshot {

    private HashMap<UUID, SimplifiedIndividual> simplifiedIndividuals;

    private Point2D[] points;
    private int[] colors;

    private long time;

    public Snapshot(HashMap<UUID, SimplifiedIndividual> simplifiedIndividuals, Point2D[] points, int[] colors,
                    long time) {
        this.simplifiedIndividuals = simplifiedIndividuals;

        this.points = points;
        this.colors = colors;

        this.time = time;
    }

    public Snapshot(long time) {
        this.simplifiedIndividuals = new HashMap<>();
        this.time = time;
    }

    public void add(UUID uuid, SimplifiedIndividual simplifiedIndividual) {
        this.simplifiedIndividuals.put(uuid, simplifiedIndividual);
    }

    public void setColors(int[] colors) {
        this.colors = colors;
    }

    public void setPoints(Point2D[] points) {
        this.points = points;
    }

    public HashMap<UUID, SimplifiedIndividual> getSimplifiedIndividuals() {
        return simplifiedIndividuals;
    }

    public Point2D[] getPoints() {
        return points;
    }

    public int[] getColors() {
        return colors;
    }

    public long getTime() {
        return time;
    }
}
