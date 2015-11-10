package xyz.ll.life;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Profiler {

    private Map<String, List<Long>> times = new HashMap<>();

    private List<Long> get(String action) {
        if (!times.containsKey(action)) {
            times.put(action, new ArrayList<>());
        }
        return times.get(action);
    }

    public void log(String action, long time) {
        // System.out.println("Time to [" + action + "] : " + time);
        get(action).add(time);
    }

    public void profile(String action, Runnable r) {
        long now = System.currentTimeMillis();
        r.run();
        log(action, System.currentTimeMillis() - now);
    }

    public void averages() {
        for (String action : times.keySet()) {
            System.out.println("Average time to [" + action + "] " + average(action));
        }
    }

    public double average(String action) {
        return times.get(action).stream().mapToInt(Long::intValue).average().orElse(0);
    }
    
    public void logAveragesOverTime() {
        Thread t = new Thread() {
            @Override
            public void run() {
                while (true) {
                    averages();
                    try {
                        Thread.sleep(20000);
                    } catch (InterruptedException e) {
                    }
                }
            }
        };
        t.setDaemon(true);
        t.start();
    }
}
