package xyz.ll.life;

import javafx.animation.AnimationTimer;

final class GameLoopWithAT extends AnimationTimer {

    private static Profiler profiler = new Profiler();

    static {
        profiler.logAveragesOverTime();
    }

    private Game game;
    private Runnable renderer;

    public GameLoopWithAT(Game game, Runnable renderer) {
        this.game = game;
        this.renderer = renderer;
    }

    @Override
    public void handle(long now) {
        profiler.profile("tick", () -> game.tick());
        if (game.isRendering()) {
            profiler.profile("render", () -> renderer.run());
        }
    }
}