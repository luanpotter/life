package xyz.ll.life.pca;

import java.util.UUID;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class SimplifiedIndividual {

    private UUID[] parents;
    private double[] genome;
    private double[] principalComponents;
    private int color;

    public SimplifiedIndividual(UUID[] parents, double[] genome, double[] principalComponents, int color) {
        this.parents = parents;
        this.genome = genome;
        this.principalComponents = principalComponents;
        this.color = color;
    }
}