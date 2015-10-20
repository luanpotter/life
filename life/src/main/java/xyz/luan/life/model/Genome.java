package xyz.luan.life.model;

public class Genome {

	private int[] genes;

	public Genome() {
		this.genes = new int[Gene.values().length];
	}

	public int get(Gene gene) {
		return genes[gene.ordinal()];
	}
}
