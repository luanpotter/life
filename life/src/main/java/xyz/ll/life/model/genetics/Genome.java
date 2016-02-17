package xyz.ll.life.model.genetics;

import com.google.common.primitives.Doubles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Genome {

    public static final double ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE = 1d;

    private TranslationGene translationGene;
    private RotationGene rotationGene;
    private ColorGene colorGene;
    private ReproductionGene reproductionGene;
    private MorfologicGene morfologicGene;
    private LifeGene lifeGene;
    private MetabolizationGene metabolizationGene;

    public Genome(double size) {
        initialGenes(size);
    }

    private Genome(Genome genome1, Genome genome2) {
        this.translationGene = genome1.translationGene.meiosis(genome2.translationGene);
        this.rotationGene = genome1.rotationGene.meiosis(genome2.rotationGene);
        this.colorGene = genome1.colorGene.meiosis(genome2.colorGene);
        this.reproductionGene = genome1.reproductionGene.meiosis(genome2.reproductionGene);
        this.morfologicGene = genome1.morfologicGene.meiosis(genome2.morfologicGene);
        this.lifeGene = genome1.lifeGene.meiosis(genome2.lifeGene);
        this.metabolizationGene = genome1.metabolizationGene.meiosis(genome2.metabolizationGene);
    }

    private void initialGenes(double size) {
        this.translationGene = new TranslationGene();
        this.rotationGene = new RotationGene();
        this.colorGene = new ColorGene();
        this.reproductionGene = new ReproductionGene();
        this.morfologicGene = new MorfologicGene(size);
        this.lifeGene = new LifeGene();
        this.metabolizationGene = new MetabolizationGene();
    }

    private List<Gene<?>> allGenes() {
        return Arrays.asList(translationGene, rotationGene, colorGene, reproductionGene, morfologicGene, lifeGene, metabolizationGene);
    }

    public double geneticDistance(Genome genome) {
        return this.translationGene.distance(genome.translationGene) + this.rotationGene.distance(genome.rotationGene)
                + this.colorGene.distance(genome.colorGene) + this.reproductionGene.distance(genome.reproductionGene)
                + this.morfologicGene.distance(genome.morfologicGene) + this.lifeGene.distance(genome.lifeGene)
                + this.metabolizationGene.distance(genome.metabolizationGene);
    }

    public boolean isCompatible(Genome genome) {
        return this.geneticDistance(genome) < Genome.ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE;
    }

    public TranslationGene getTranslation() {
        return this.translationGene;
    }

    public RotationGene getRotation() {
        return this.rotationGene;
    }

    public ColorGene getColor() {
        return this.colorGene;
    }

    public ReproductionGene getReproduction() {
        return this.reproductionGene;
    }

    public MorfologicGene getMorfological() {
        return this.morfologicGene;
    }

    public LifeGene getLife() {
        return this.lifeGene;
    }

    public MetabolizationGene getMetabolization() {
        return this.metabolizationGene;
    }

    public Genome meiosis(Genome genome) {
        return new Genome(this, genome);
    }

    public double[] getValue() {
        List<Double> values = new ArrayList<>();
        for (Gene<?> gene : allGenes()) {
            values.addAll(gene.getValues());
        }
        return Doubles.toArray(values);
    }
}
