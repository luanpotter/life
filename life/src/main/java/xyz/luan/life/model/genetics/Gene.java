package xyz.luan.life.model.genetics;

/**
 * Created by lucas-cleto on 10/27/15.
 */
public interface Gene<T extends Gene> {

    public static final double MUTATION_PROBABILITY = 0.1;

    public T meiosis(T gene);
}
