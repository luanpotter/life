package xyz.luan.life.model;

import javafx.scene.paint.Color;

import java.util.HashMap;
import java.util.Map;

public class Genome {

    private Map<Gene, Double> genes;

    public Genome() {
        genes = new HashMap<>();
        randomGenes();
    }

    private void randomGenes() {
        genes.put(Gene.TRANSLATION_SPEED, 30d);
        genes.put(Gene.TRANSLATION_CONSTANCY, 2d);
        genes.put(Gene.ROTATION_SPEED, 90d);
        genes.put(Gene.ROTATION_CONSTANCY, 5d);
        genes.put(Gene.COLOR, 1d);
        genes.put(Gene.CHARITY, 20d);
        genes.put(Gene.LIBIDO, 1d);

        genes.put(Gene.A, 10d); genes.put(Gene.B, 1d);
        genes.put(Gene.C, 100d); genes.put(Gene.D, 10d); genes.put(Gene.E, 10d);
        genes.put(Gene.F, 10d); genes.put(Gene.G, 1d);
        genes.put(Gene.H, 1d); genes.put(Gene.I, 10d);
        genes.put(Gene.J, 0d); genes.put(Gene.K, 5d); genes.put(Gene.L, 5d);
        genes.put(Gene.M, 1d); genes.put(Gene.N, 10d);
    }

    public double get(Gene gene) {
        return genes.get(gene);
    }

    public int numberOfGenes() {
        return genes.size();
    }

    public Map<Gene, Double> getGenes() {
        return genes;
    }

    public double geneticDistance(Genome genome) {
        //boolean sameGeneTypes = this.getGenes().keySet().equals(genome.getGenes());
        boolean sameGeneTypes = true;
        if (sameGeneTypes) {
            double sum = 0;
            for (Gene gene : this.getGenes().keySet()) {
                sum += Math.abs(Math.pow(this.get(gene) - genome.get(gene), 4));
            }
            return sum;
        } else {
            return Util.INFINITY;
        }
    }
}
