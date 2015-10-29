package xyz.ll.life.model.genetics;

public class Genome {

    public static final double ACCEPTABLE_GENETIC_DISTANCE_TO_REPRODUCE = 10d;

    private TranslationGene translationGene;
    private RotationGene rotationGene;
    private ColorGene colorGene;
    private ReproductionGene reproductionGene;
    private MorfologicGene morfologicGene;

    public Genome() {
        initialGenes();
    }

    private Genome(Genome genome1, Genome genome2) {
        this.translationGene = genome1.translationGene.meiosis(genome2.translationGene);
        this.rotationGene = genome1.rotationGene.meiosis(genome2.rotationGene);
        this.colorGene = genome1.colorGene.meiosis(genome2.colorGene);
        this.reproductionGene = genome1.reproductionGene.meiosis(genome2.reproductionGene);
        this.morfologicGene = genome1.morfologicGene.meiosis(genome2.morfologicGene);
    }

    private void initialGenes() {
        translationGene = new TranslationGene();
        rotationGene = new RotationGene();
        colorGene = new ColorGene();
        reproductionGene = new ReproductionGene();
        morfologicGene = new MorfologicGene();
    }

    public double geneticDistance(Genome genome) {
        return this.translationGene.distance(genome.translationGene) +
                this.rotationGene.distance(genome.rotationGene) +
                this.colorGene.distance(genome.colorGene) +
                this.reproductionGene.distance(genome.reproductionGene) +
                this.morfologicGene.distance(genome.morfologicGene);
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
