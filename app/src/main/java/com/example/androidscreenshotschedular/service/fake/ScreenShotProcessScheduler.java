package com.example.androidscreenshotschedular.service.fake;


import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.TextView;

public class ScreenShotProcessScheduler implements Runnable {
    private long timeInMilliSeconds;
    private Handler postTaskPeriodHandler;
    private Handler mainUiHandler;
    private TextView feedBackView;
    private int counter;

    public ScreenShotProcessScheduler(long timeInMilliSecond, TextView feedBackView) {
        this.timeInMilliSeconds = timeInMilliSecond;
        this.feedBackView = feedBackView;
        mainUiHandler = new Handler(Looper.getMainLooper());
    }

    public void start() {
        postTaskPeriodHandler = new Handler(startAndGetBackGroundHandlerThread().getLooper());
        postTaskPeriodHandler.postDelayed(this, timeInMilliSeconds);
    }


    private HandlerThread startAndGetBackGroundHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("BackGround Handler");
        handlerThread.start();
        return handlerThread;
    }

    @Override
    public void run() {
        mainUiHandler.post(new Runnable() {
            @Override
            public void run() {
                feedBackView.setText("" + counter);
                counter++;
            }
        });
        postTaskPeriodHandler.postDelayed(this, timeInMilliSeconds);
    }


}
