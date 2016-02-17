package xyz.ll.life.pca;

import java.util.UUID;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class SimplifiedIndividual {

    private UUID[] parents;
    private double[] coordinates;
    private int color;

    public SimplifiedIndividual(UUID[] parents, double[] coordinates, int color) {
        this.parents = parents;
        this.coordinates = coordinates;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public UUID[] getParents() {
        return parents;
    }
}