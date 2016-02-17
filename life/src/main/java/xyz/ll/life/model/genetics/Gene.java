package xyz.ll.life.model.genetics;

import java.util.List;

/**
 * Created by lucas-cleto on 10/27/15.
 */
public interface Gene<T extends Gene<T>> {

    public static final double MUTATION_PROBABILITY = 0.9;

    public void mutation();

    public T meiosis(T gene);

    public double distance(T gene);

    public List<Double> getValues();
}
