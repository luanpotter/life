package xyz.ll.life.model.world;

import xyz.luan.geometry.Point;
import xyz.luan.geometry.Polygon;
import xyz.luan.geometry.Rectangle;

public enum WorldTypes {
    NO_WALLS {
        @Override
        public void build(World world) {
        }
    }, BOX {
        @Override
        public void build(World world) {
            double w = world.getBorders().getWidth();
            double h = world.getBorders().getHeight();
            double s = 10;
            world.getWalls().add(new Wall(new Rectangle(0, w, 0, s)));
            world.getWalls().add(new Wall(new Rectangle(0, w, h - s, h)));
            world.getWalls().add(new Wall(new Rectangle(0, s, s, h - s)));
            world.getWalls().add(new Wall(new Rectangle(w - s, w, s, h - s)));
        }
    }, TWO_AREAS {
        @Override
        public void build(World world) {
            double w = world.getBorders().getWidth();
            double h = world.getBorders().getHeight();
            double s = 10;
            double hs = 20;

            BOX.build(world);

            world.getWalls().add(new Wall(new Rectangle(s, (w - hs)/2, (h - s)/2, (h + s)/2)));
            world.getWalls().add(new Wall(new Rectangle((w + hs)/2, w - s, (h - s)/2, (h + s)/2)));
        }
    }, RING {
        @Override
        public void build(World world) {
            double w = world.getBorders().getWidth();
            double h = world.getBorders().getHeight();
            int sides = 20;
            double size = (Math.min(w, h) - 150)/2;

            Point origin = new Point(w/2, h/2), p1 = new Point(origin.x, origin.y + size);

            BOX.build(world);
            for (int i = 0; i < sides; i++) {
                Point p2 = p1.rotateTo(origin, +2 * Math.PI / sides);
                world.getWalls().add(new Wall(new Polygon(origin, p1, p2)));
                p1 = p2;
            }
        }
    };

    public abstract void build(World world);
}
