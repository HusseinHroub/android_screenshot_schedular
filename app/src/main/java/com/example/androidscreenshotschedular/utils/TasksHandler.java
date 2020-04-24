package com.example.androidscreenshotschedular.utils;

import android.os.Handler;
import android.os.Looper;

public class TasksHandler extends Handler {
    private Runnable t;

    public TasksHandler(Looper looper) {
        super(looper);
    }

    public void postTask(Runnable runnable) {
        super.post(runnable);
    }

    public void postTaskEach(final Runnable runnable, final long timeInMilliSeconds) {
        t = new Runnable() {
            @Override
            public void run() {

                runnable.run();
                HelperUtil.printLog("TasksHandler.run: nice finished okay wonderful repeating");//TODO remove
                postDelayed(this, timeInMilliSeconds);


            }
        };
        postDelayed(t, timeInMilliSeconds);
    }

    public void stopRepetitiveTask() {
        removeCallbacks(t);
    }

}
