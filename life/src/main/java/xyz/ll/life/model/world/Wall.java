package xyz.ll.life.model.world;

import javafx.scene.paint.Color;
import xyz.ll.life.model.Entity;
import xyz.ll.life.model.EntityShape;
import xyz.luan.geometry.Shape;

public class Wall extends Entity {

    public static final Color DEFAULT_COLOR = Color.WHITE;

    public Wall(Shape rectangle) {
        this.body = new EntityShape(rectangle);
        this.body.setColor(DEFAULT_COLOR);
    }
}
