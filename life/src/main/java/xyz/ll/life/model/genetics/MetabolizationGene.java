package xyz.ll.life.model.genetics;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class MetabolizationGene implements Gene<MetabolizationGene> {

    private static final double EFFICIENCY_MAX = 0.9d, EFFICIENCY_MIN = 0d, EFFICIENCY_VARIANCE = 0.05d;
    private static final double AREA_PROPORTION_MAX = 0.5d, AREA_PROPORTION_MIN = 0d, AREA_PROPORTION_VARIANCE = 0.05d;

    private double efficiency;
    private double areaProportion;

    @Override
    public void mutation() {

    }

    @Override
    public MetabolizationGene meiosis(MetabolizationGene gene) {
        return null;
    }

    @Override
    public double distance(MetabolizationGene gene) {
        return 0;
    }
}
