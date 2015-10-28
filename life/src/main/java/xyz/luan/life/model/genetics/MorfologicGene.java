package xyz.luan.life.model.genetics;

/**
 * Created by lucas-cleto on 10/28/15.
 */
public class MorfologicGene implements Gene<MorfologicGene> {

    private double[] characteristics;

    @Override
    public void mutation() {

    }

    @Override
    public MorfologicGene meiosis(MorfologicGene gene) {
        return null;
    }

    @Override
    public double distance(MorfologicGene gene) {
        return 0;
    }
}
