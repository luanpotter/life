package xyz.ll.life;

import javafx.animation.AnimationTimer;

final class GameLoopWithAT extends AnimationTimer {

    private static Profiler profiler = new Profiler();

    static {
        profiler.logAveragesOverTime();
    }

    private long lastRenderTick = 0;

    private Game game;
    private Runnable renderer;

    public GameLoopWithAT(Game game, Runnable renderer) {
        this.game = game;
        this.renderer = renderer;
    }

    @Override
    public void handle(long now) {
        long diff = now - lastRenderTick;
        profiler.profile("tick", () -> game.tick());
        if (diff > 60) { // TODO make this properly
            profiler.profile("render", () -> renderer.run());
            lastRenderTick = now;
        }
    }
}