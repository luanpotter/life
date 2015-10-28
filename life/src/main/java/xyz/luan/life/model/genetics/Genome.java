package xyz.luan.life.model.genetics;

import java.util.HashMap;
import java.util.Map;

import xyz.luan.life.model.Gene2;

public class Genome {

    private Map<Gene2, Double> genes2;

    private Map<Class<? extends Gene<?>>, Gene<?>> genes;

    public Genome() {
        genes = new HashMap<>();
        genes2 = new HashMap<>();
        randomGenes();
    }

    public Genome(Genome genome1, Genome genome2) {
        genes = new HashMap<>();
        for (Class<? extends Gene<?>> gene : genome1.genes.keySet()) {
            // genes.put(gene, genome1.get(gene).meiosis(genome2.get(gene)));
        }
    }

    private void randomGenes() {
        genes2.put(Gene2.A, 10d);
        genes2.put(Gene2.B, 1d);
        genes2.put(Gene2.C, 0d);
        genes2.put(Gene2.D, 0d);
        genes2.put(Gene2.E, 0d);
        genes2.put(Gene2.F, 10d);
        genes2.put(Gene2.G, 1d);
        genes2.put(Gene2.H, 3d);
        genes2.put(Gene2.I, 10d);
        genes2.put(Gene2.J, 0d);
        genes2.put(Gene2.K, 0d);
        genes2.put(Gene2.L, 0d);
        genes2.put(Gene2.M, 1d);
        genes2.put(Gene2.N, 40d);

        genes.put(TranslationGene.class, new TranslationGene());
        genes.put(RotationGene.class, new RotationGene());
        genes.put(ColorGene.class, new ColorGene());
        genes.put(ReproductionGene.class, new ReproductionGene());
        genes.put(MorfologicGene.class, new MorfologicGene());
    }

    public double get(Gene2 gene) {
        return gene.getCoefficient() * genes2.get(gene);
    }

    public int numberOfGenes() {
        return genes2.size();
    }

    public Map<Gene2, Double> getGenes() {
        return genes2;
    }

    public double geneticDistance(Genome genome) {
        double sum = 0;
        for (Gene2 gene : this.getGenes().keySet()) {
            sum += Math.abs(Math.pow(this.get(gene) - genome.get(gene), 2));
        }
        return sum;
    }

    @SuppressWarnings("unchecked")
    public <T extends Gene<?>> T get(Class<T> clazz) {
        return (T) genes.get(clazz);
    }

    public TranslationGene getTranslation() {
        return get(TranslationGene.class);
    }

    public RotationGene getRotation() {
        return get(RotationGene.class);
    }

    public ColorGene getColor() {
        return get(ColorGene.class);
    }

    public ReproductionGene getReproduction() {
        return get(ReproductionGene.class);
    }

    public MorfologicGene getMorfological() {
        return get(MorfologicGene.class);
    }

    public Genome meiosis(Genome genome) {
        return new Genome(this, genome);
    }
}
