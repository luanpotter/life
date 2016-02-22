package xyz.ll.life.pca;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by lucas-cleto on 2/16/16.
 */
public class Snapshot {


    private List<PCAPoint> points;
    private long time;

    public Snapshot(long time) {
        this.time = time;
        this.points = new ArrayList<>();
    }

    public void add(PCAPoint point) {
        this.points.add(point);
    }

    public List<PCAPoint> getPoints() {
        return points;
    }

    public PCAPoint findPoint(UUID individualId) {
        for (PCAPoint point : points) {
            if (point.getIndividualId().equals(individualId)) {
                return point;
            }
        }
        return null;
    }
}
