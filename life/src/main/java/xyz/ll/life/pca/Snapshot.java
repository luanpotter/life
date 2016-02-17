package xyz.ll.life.pca;

import java.util.HashMap;
import java.util.UUID;

/**
 * Created by lucas-cleto on 2/16/16.
 */
public class Snapshot {

    private HashMap<UUID, SimplifiedIndividual> simplifiedIndividuals;
    private long time;

    public Snapshot(HashMap<UUID, SimplifiedIndividual> simplifiedIndividuals, long time) {
        this.simplifiedIndividuals = simplifiedIndividuals;
        this.time = time;
    }
}
