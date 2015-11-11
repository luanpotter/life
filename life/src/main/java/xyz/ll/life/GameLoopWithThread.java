package xyz.ll.life;

final class GameLoopWithThread extends Thread {

    private static Profiler profiler = new Profiler();

    static {
        profiler.logAveragesOverTime();
    }

    private long lastRenderTick = 0;

    private Game game;
    private Runnable renderer;

    public GameLoopWithThread(Game game, Runnable renderer) {
        this.game = game;
        this.renderer = renderer;
    }

    @Override
    public void run() {
        while (true) {
            it();
        }
    }

    private void it() {
        long diff = System.currentTimeMillis() - lastRenderTick;
        profiler.profile("tick", () -> game.tick());
        if (diff > 1000/30) {
            profiler.profile("render", () -> renderer.run());
            lastRenderTick = System.currentTimeMillis();
        }
    }
}