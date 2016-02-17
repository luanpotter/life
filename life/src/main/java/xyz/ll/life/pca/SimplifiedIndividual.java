package xyz.ll.life.pca;

import java.util.UUID;

/**
 * Created by lucas-cleto on 2/17/16.
 */
public class SimplifiedIndividual {

    private UUID uuid;
    private UUID[] parents;
    private double[] genome;
    private double[] principalComponents;
    private int specie;

    public SimplifiedIndividual(UUID uuid, UUID[] parents, double[] genome, double[] principalComponents, int specie) {
        this.uuid = uuid;
        this.parents = parents;
        this.genome = genome;
        this.principalComponents = principalComponents;
        this.specie = specie;
    }
}