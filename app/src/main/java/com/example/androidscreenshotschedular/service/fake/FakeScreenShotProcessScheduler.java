package com.example.androidscreenshotschedular.service.fake;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.TextView;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.BitMapSaving;

public class FakeScreenShotProcessScheduler implements Runnable {
    private long timeInMilliSeconds;
    private Handler postTaskPeriodHandler;
    private TextView feedBackView;
    private int counter;
    private Context context;
    private Handler mainUiHandler;

    public FakeScreenShotProcessScheduler(long timeInMilliSecond, TextView feedBackView, Context context) {
        this.timeInMilliSeconds = timeInMilliSecond;
        this.feedBackView = feedBackView;
        this.context = context;
        mainUiHandler = new Handler(Looper.getMainLooper());

    }

    public void start() {
        postTaskPeriodHandler = new Handler(startAndGetBackGroundHandlerThread().getLooper());
        postTaskPeriodHandler.postDelayed(this, timeInMilliSeconds);
    }


    private HandlerThread startAndGetBackGroundHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("BackGround Handler");//TODO should be closed when it needs to be closed.
        handlerThread.start();
        return handlerThread;
    }

    @Override
    public void run() {
        BitMapSaving.saveBitMap(getOurTestImageBitMap());
        sendFeedBackToMainUI();
        postTaskPeriodHandler.postDelayed(this, timeInMilliSeconds);
    }

    private Bitmap getOurTestImageBitMap() {
        return BitmapFactory.decodeResource(context.getResources(),
                R.drawable.gray_color_drawable);
    }

    private void sendFeedBackToMainUI() {
        mainUiHandler.post(new Runnable() {
            @Override
            public void run() {
                counter++;
                feedBackView.setText("" + counter);
            }
        });
    }

}
