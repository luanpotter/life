package xyz.luan.life.model;

import java.util.HashMap;
import java.util.Map;

public class Genome {

    private Map<Gene, Double> genes;

	public Genome() {
		genes = new HashMap<>();
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
        boolean sameGeneTypes = this.getGenes().keySet().equals(genome.getGenes());
        if (sameGeneTypes) {
            double sum = 0;
            for (Gene gene : this.getGenes().keySet()) {
                sum += Math.pow(this.get(gene) - genome.get(gene), 2);
            }
            return sum;
        } else {
            return Util.INFINITY;
        }
    }
}
