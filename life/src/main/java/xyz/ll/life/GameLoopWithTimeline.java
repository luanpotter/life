package xyz.ll.life;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

final class GameLoopWithTimeline {

    private static Profiler profiler = new Profiler();

    static {
        profiler.logAveragesOverTime();
    }

    private long lastRenderTick = 0;

    private Game game;
    private Runnable renderer;

    public GameLoopWithTimeline(Game game, Runnable renderer) {
        this.game = game;
        this.renderer = renderer;
    }

    public void start() {
        final Duration oneFrameAmt = Duration.millis(1000 / 60);
        final KeyFrame oneFrame = new KeyFrame(oneFrameAmt, (e) -> {
            long diff = System.currentTimeMillis() - lastRenderTick;
            profiler.profile("tick", () -> game.tick());
            if (diff > 60) { // TODO make this properly
                profiler.profile("render", () -> renderer.run());
                lastRenderTick = System.currentTimeMillis();
            }
        }); // oneFrame

        new Timeline(oneFrame).play();
    }
}