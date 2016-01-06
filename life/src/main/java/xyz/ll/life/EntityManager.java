package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;

import xyz.ll.life.model.Organic;

public class EntityManager {

    private List<Organic> added = new ArrayList<>();
    private List<Organic> removed = new ArrayList<>();

    public void add(Organic e) {
        added.add(e);
    }

    public void remove(Organic e) {
        removed.add(e);
    }

    public List<Organic> getAdded() {
        return added;
    }

    public List<Organic> getRemoved() {
        return removed;
    }

    public boolean deceased(Organic e) {
        return removed.contains(e);
    }

    public void clear() {
        added.clear();
        removed.clear();
    }
}