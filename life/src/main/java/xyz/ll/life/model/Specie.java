package xyz.ll.life.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lucas-cleto on 11/3/15.
 */
public class Specie {
    private List<Individual> individuals;

    public Specie() {
        this.individuals = new ArrayList<>();
    }

    public boolean matches(Individual i) {
        for (Individual s : this.individuals) {
            if (!s.getGenome().isCompatible(i.getGenome())) {
                return false;
            }
        }
        return true;
    }

    public void add(Individual i) {
        this.individuals.add(i);
    }

    @Override
    public String toString() {
        return String.valueOf(individuals.size());
    }
}
