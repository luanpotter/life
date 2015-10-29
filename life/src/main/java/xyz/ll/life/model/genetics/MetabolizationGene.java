package xyz.ll.life.model.genetics;

/**
 * Created by lucas-cleto on 10/29/15.
 */
public class MetabolizationGene implements Gene<MetabolizationGene> {
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
