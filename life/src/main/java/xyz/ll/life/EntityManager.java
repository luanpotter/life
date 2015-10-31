package xyz.ll.life;

import java.util.ArrayList;
import java.util.List;

import xyz.ll.life.model.Entity;

public class EntityManager {

    private List<Entity> added = new ArrayList<>();
    private List<Entity> removed = new ArrayList<>();

    public void add(Entity e) {
        added.add(e);
    }

    public void remove(Entity e) {
        removed.add(e);
    }

    public List<Entity> getAdded() {
        return added;
    }

    public List<Entity> getRemoved() {
        return removed;
    }

    public boolean alive(Entity e) {
        return !removed.contains(e);
    }
}