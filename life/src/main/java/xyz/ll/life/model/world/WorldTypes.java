package xyz.ll.life.model.world;

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
    };

    public abstract void build(World world);
}
