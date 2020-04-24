package com.example.androidscreenshotschedular.utils;

import android.os.Handler;
import android.os.Looper;

public class TasksHandler extends Handler {
    private Runnable forRepetitionTask;

    public TasksHandler(Looper looper) {
        super(looper);
    }

    public void postTask(Runnable runnable) {
        super.post(runnable);
    }

    public void postTaskEach(final Runnable runnable, final long timeInMilliSeconds) {
        forRepetitionTask = new Runnable() {
            @Override
            public void run() {

                runnable.run();
                HelperUtil.printLog("TasksHandler.run: nice finished okay wonderful repeating");
                postDelayed(this, timeInMilliSeconds);


            }
        };
        postDelayed(forRepetitionTask, timeInMilliSeconds);
    }

    public void stopRepetitiveTask() {
        removeCallbacks(forRepetitionTask);
    }

}
