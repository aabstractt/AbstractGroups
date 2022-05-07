package dev.thatsmybaby.shared;

import java.util.concurrent.ForkJoinPool;

public final class TaskUtils {

    public static void runAsync(Runnable runnable) {
        if (AbstractPlugin.getInstance().isPrimaryThread()) {
            ForkJoinPool.commonPool().execute(runnable);

            return;
        }

        runnable.run();
    }

    public static void runLaterAsync(Runnable runnable, int delay) {

    }

    public void runLater(Runnable runnable, int delay) {

    }

    public void run(Runnable runnable) {

    }
}