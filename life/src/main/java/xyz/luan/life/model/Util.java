package xyz.luan.life.model;

import javafx.scene.paint.Color;

import java.util.Map.Entry;

public class Util {

    public static final Color[] COLORS = {Color.RED, Color.GREEN, Color.BLUE};
    public static final double INFINITY = Double.POSITIVE_INFINITY;

    public static double geneticDistance(Individual i1, Individual i2) {
        boolean sameGeneTypes = i1.getGenome().getGenes().keySet().equals(i2.getGenome().getGenes());
        if (sameGeneTypes) {
            double sum = 0;
            for (Gene gene : i1.getGenome().getGenes().keySet()) {
                sum += Math.pow(i1.getGenome().get(gene) - i2.getGenome().get(gene), 2);
            }
            return sum;
        } else {
            return INFINITY;
        }
    }
}
