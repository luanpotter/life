package xyz.ll.life.model;

import javafx.scene.canvas.GraphicsContext;
import xyz.ll.life.model.EntityShape;
import xyz.luan.geometry.Shape;

/**
 * Created by lucas-cleto on 1/5/16.
 */
public abstract class Entity {

    protected EntityShape body;

    public void draw(GraphicsContext g) {
        body.draw(g);
    }

    public EntityShape getBody() {
        return body;
    }

    public Shape intersection(Entity entity) {
        return body.getShape().intersection(entity.body.getShape());
    }
}
