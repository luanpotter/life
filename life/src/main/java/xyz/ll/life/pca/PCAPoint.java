package xyz.ll.life.pca;

import lombok.Data;
import xyz.luan.geometry.Point;

import java.util.UUID;

/**
 * Created by luan on 22/02/16.
 */
@Data
public class PCAPoint {

    private UUID individualId;
    private Point point;
    private int species;

    public PCAPoint(UUID individualId, Point point, int species) {
        this.individualId = individualId;
        this.point = point;
        this.species = species;
    }

}
