package xyz.ll.life.pca;

import java.util.ArrayList;
import java.util.List;

import xyz.ll.life.model.Individual;
import xyz.ll.life.model.Species;

public class SpeciesClassifier {

    private List<Species> all;

    public SpeciesClassifier() {
        this.all = new ArrayList<>();
    }

    public int getSpecies(Individual i) {
        for (int j = 0; j < all.size(); j++) {
            Species s = all.get(j);
            if (s.matches(i)) {
                s.add(i);
                return j;
            }
        }

        Species newSpecies = new Species();
        newSpecies.add(i);
        all.add(newSpecies);
        return all.size() - 1;

    }
}
