package com.example.androidscreenshotschedular.utils;

import android.os.Handler;
import android.os.Looper;

public class TasksHandler extends Handler {
    public TasksHandler(Looper looper) {
        super(looper);
    }

    public void postTask(Runnable runnable) {
        super.post(runnable);
    }

    public void postTaskEach(final Runnable runnable, final long timeInMilliSeconds) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                runnable.run();
                postDelayed(this, timeInMilliSeconds);
            }
        }, timeInMilliSeconds);
    }

}
