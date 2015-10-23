package xyz.luan.life.model;

import java.util.Random;

public class Individual extends Entity {

	private Genome genome;

    private Individual(Genome genome) {
        this.genome = genome;
    }

    public Genome getGenome() {
        return genome;
    }

    private Individual reproduce(Individual pair) {
        if (Util.geneticDistance(this, pair) < 2) {
            Random random = new Random();
            Genome genome = new Genome();
            for (Gene gene : this.getGenome().getGenes().keySet()) {
                double a = this.getGenome().get(gene);
                double b = pair.getGenome().get(gene);
                double diff = Math.abs(a - b);
                double mix = Math.min(a, b) + diff * random.nextDouble();
                if (random.nextInt(10) == 0) {
                    mix = mix + random.nextDouble() * Math.pow(-1, random.nextInt(1));
                }
                genome.getGenes().put(gene, mix);
            }

            Individual child = new Individual(genome);
            return child;
        } else {
            return null;
        }
    }

    @Override
    public void tick() {
    }
}
