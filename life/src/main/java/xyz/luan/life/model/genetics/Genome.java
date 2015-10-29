package xyz.luan.life.model.genetics;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import xyz.luan.life.model.Gene2;
import xyz.luan.life.model.Util;

public class Genome {

    private Map<Gene2, Double> genes2;

    private TranslationGene translationGene;
    private RotationGene rotationGene;
    private ColorGene colorGene;
    private ReproductionGene reproductionGene;
    private MorfologicGene morfologicGene;

    public Genome() {
        genes2 = new HashMap<>();
        randomGenes();
    }

    private Genome(Genome genome1, Genome genome2) {
        oldGenesMeiosis(genome1, genome2);

        this.translationGene = genome1.translationGene.meiosis(genome2.translationGene);
        this.rotationGene = genome1.rotationGene.meiosis(genome2.rotationGene);
        this.colorGene = genome1.colorGene.meiosis(genome2.colorGene);
        this.reproductionGene = genome1.reproductionGene.meiosis(genome2.reproductionGene);
        this.morfologicGene = genome1.morfologicGene.meiosis(genome2.morfologicGene);
    }

    private void oldGenesMeiosis(Genome genome1, Genome genome2) {
        Random random = new Random();
        genes2 = new HashMap<>();
        for (Gene2 gene : Gene2.values()) {
            double a = genome1.get(gene);
            double b = genome2.get(gene);
            double diff = Math.abs(a - b);
            double mix = Math.min(a, b) + diff * random.nextDouble();
            if (random.nextInt(Util.RARITY_OF_IMMUTABILITY) == 0) {
                mix = mix + random.nextDouble() * Math.pow(-1, random.nextInt(1));
            }
            genes2.put(gene, Math.abs(mix));
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

        translationGene = new TranslationGene();
        rotationGene = new RotationGene();
        colorGene = new ColorGene();
        reproductionGene = new ReproductionGene();
        morfologicGene = new MorfologicGene();
    }

    public double get(Gene2 gene) {
        return genes2.get(gene);
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

    public TranslationGene getTranslation() {
        return translationGene;
    }

    public RotationGene getRotation() {
        return rotationGene;
    }

    public ColorGene getColor() {
        return colorGene;
    }

    public ReproductionGene getReproduction() {
        return reproductionGene;
    }

    public MorfologicGene getMorfological() {
        return morfologicGene;
    }

    public Genome meiosis(Genome genome) {
        return new Genome(this, genome);
    }
}
