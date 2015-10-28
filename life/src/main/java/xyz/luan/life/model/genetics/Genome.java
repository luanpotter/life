package xyz.luan.life.model.genetics;

import xyz.luan.life.model.Gene2;
import xyz.luan.life.model.Util;

import java.util.HashMap;
import java.util.Map;

public class Genome {

    private Map<Gene2, Double> genes;
    private TranslationGene translationGene;

    public Genome() {
        genes = new HashMap<>();
        randomGenes();
    }

    private void randomGenes() {
        genes.put(Gene2.TRANSLATION_SPEED, 0.5);
        genes.put(Gene2.TRANSLATION_CONSTANCY, 0.5);
        genes.put(Gene2.ROTATION_SPEED, 0.5);
        genes.put(Gene2.ROTATION_CONSTANCY, 0.5);
        genes.put(Gene2.COLOR, 3 * Math.PI / 2);
        genes.put(Gene2.CHARITY, 100d);
        genes.put(Gene2.LIBIDO, 1d);

        genes.put(Gene2.A, 10d); genes.put(Gene2.B, 1d);
        genes.put(Gene2.C, 0d); genes.put(Gene2.D, 0d); genes.put(Gene2.E, 0d);
        genes.put(Gene2.F, 10d); genes.put(Gene2.G, 1d);
        genes.put(Gene2.H, 3d); genes.put(Gene2.I, 10d);
        genes.put(Gene2.J, 0d); genes.put(Gene2.K, 0d); genes.put(Gene2.L, 0d);
        genes.put(Gene2.M, 1d); genes.put(Gene2.N, 40d);

        translationGene = new TranslationGene();
    }

    public double get(Gene2 gene) {
        return gene.getCoefficient() * genes.get(gene);
    }

    public int numberOfGenes() {
        return genes.size();
    }

    public Map<Gene2, Double> getGenes() {
        return genes;
    }

    public double geneticDistance(Genome genome) {
        //boolean sameGeneTypes = this.getGenes().keySet().equals(genome.getGenes());
        boolean sameGeneTypes = true;
        if (sameGeneTypes) {
            double sum = 0;
            for (Gene2 gene : this.getGenes().keySet()) {
                sum += Math.abs(Math.pow(this.get(gene) - genome.get(gene), 2));
            }
            return sum;
        } else {
            return Util.INFINITY;
        }
    }

    public TranslationGene getTranslationGene() {
        return translationGene;
    }
}
