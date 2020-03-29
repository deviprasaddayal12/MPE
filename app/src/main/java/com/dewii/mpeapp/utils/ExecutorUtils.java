package com.dewii.mpeapp.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class ExecutorUtils {
    public static final String TAG = ExecutorUtils.class.getCanonicalName();

    public static final int THREAD_POOL_SIZE = 20;

    private static final Object INSTANCE_LOCK = new Object();

    private static ExecutorUtils instance;

    private Executor pool;
    private Executor single;
    private Main main;

    public static ExecutorUtils getInstance() {
        if (instance == null) {
            synchronized (INSTANCE_LOCK) {
                instance = new ExecutorUtils(Executors.newFixedThreadPool(THREAD_POOL_SIZE),
                        Executors.newSingleThreadExecutor(),
                        new Main());
            }
        }

        return instance;
    }

    private ExecutorUtils(Executor pool, Executor single, Main main) {
        this.pool = pool;
        this.single = single;
        this.main = main;
    }

    public void executeOnPool(Runnable command) {
        pool.execute(command);
    }

    public void executeOnSingle(Runnable command) {
        single.execute(command);
    }

    public void executeOnMain(Runnable command) {
        main.execute(command);
    }

    public void executeOnMainDelayed(Runnable command, long delayMillis) {
        main.executeDelayed(command, delayMillis);
    }

    private static class Main implements Executor {
        private Handler handler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(Runnable command) {
            handler.post(command);
        }

        public void executeDelayed(Runnable command, long delayMillis) {
            handler.postDelayed(command, delayMillis);
        }
    }

    /*This executes from the background thread*/
    public static void executeAfterMillis(@NonNull final Runnable runnable, long millis) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runnable.run();
            }
        }, millis);
    }
}
