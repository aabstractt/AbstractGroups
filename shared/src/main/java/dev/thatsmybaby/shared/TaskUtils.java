package dev.thatsmybaby.shared;

import java.util.concurrent.ForkJoinPool;

public final class TaskUtils {

    public static void runAsync(Runnable runnable) {
        if (isPrimaryThread()) {
            ForkJoinPool.commonPool().execute(runnable);

            return;
        }

        runnable.run();
    }

    public static boolean isPrimaryThread() {
        return false;
    }
}