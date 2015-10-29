package xyz.ll.life.model.genetics;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class LifeGene implements Gene<LifeGene> {

    private static final double HEALTH_MAX = 1, HEALTH_MIN = 0, HEALTH_VARIANCE = 0.05;

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
