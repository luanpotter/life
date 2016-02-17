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

    public Snapshot(long time) {
        this.simplifiedIndividuals = new HashMap<>();
        this.time = time;
    }

    public void add(UUID uuid, SimplifiedIndividual simplifiedIndividual) {
        this.simplifiedIndividuals.put(uuid, simplifiedIndividual);
    }
}
