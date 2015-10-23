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
}
