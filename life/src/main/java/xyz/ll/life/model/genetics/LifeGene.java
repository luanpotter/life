package xyz.ll.life.model.genetics;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class LifeGene implements Gene<LifeGene> {

    private double health;

    @Override
    public void mutation() {

    }

    @Override
    public LifeGene meiosis(LifeGene gene) {
        return null;
    }

    @Override
    public double distance(LifeGene gene) {
        return 0;
    }
}
