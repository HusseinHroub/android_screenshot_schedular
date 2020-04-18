package com.example.androidscreenshotschedular.service.real;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.widget.TextView;
import com.example.androidscreenshotschedular.utils.BitMapSaving;
import com.example.androidscreenshotschedular.utils.TasksHandler;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class RealScreenShotProcessScheduler {
    private long timeInMilliSeconds;
    private TasksHandler postTasksHandler;
    private TextView feedBackView;
    private Context context;
    private int counter;
    private Handler mainUiHandler;
    private Socket clientSocket;

    public RealScreenShotProcessScheduler(long timeInMilliSecond, TextView feedBackView, Context context) {
        this.timeInMilliSeconds = timeInMilliSecond;
        this.feedBackView = feedBackView;
        this.context = context;
        mainUiHandler = new Handler(Looper.getMainLooper());

    }

    public void start() {
        postTasksHandler = new TasksHandler(startAndGetBackGroundHandlerThread().getLooper());
        postTasksHandler.postTask(getConnectToServerTask());
        postTasksHandler.postTaskEach(getProcessTask(), timeInMilliSeconds);
    }

    private HandlerThread startAndGetBackGroundHandlerThread() {
        HandlerThread handlerThread = new HandlerThread("ImageSchedulerHandler");//TODO should be closed when it needs to be closed.
        handlerThread.start();
        return handlerThread;
    }


    private Runnable getConnectToServerTask() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    clientSocket = new Socket("192.168.1.176", 8888);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
    }

    private Runnable getProcessTask() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    sendRequestForScreenShot();
                    BitMapSaving.saveBitMap(getPcScreenShotBitMap());
                    sendFeedBackToMainUI();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        };
    }

    private void sendRequestForScreenShot() throws IOException {
        byte[] myMessageToServer = "rs".getBytes();
        OutputStream outputStream = clientSocket.getOutputStream();
        outputStream.write(getNumberInBytes(myMessageToServer.length));
        outputStream.write(myMessageToServer);
    }

    private byte[] getNumberInBytes(int number) {
        byte[] toSendLenBytes = new byte[4];
        toSendLenBytes[0] = (byte) (number & 0xff);
        toSendLenBytes[1] = (byte) ((number >> 8) & 0xff);
        toSendLenBytes[2] = (byte) ((number >> 16) & 0xff);
        toSendLenBytes[3] = (byte) ((number >> 24) & 0xff);
        return toSendLenBytes;
    }


    private Bitmap getPcScreenShotBitMap() throws IOException {
        InputStream inputStream = clientSocket.getInputStream();
        byte[] lenBytes = new byte[4];
        inputStream.read(lenBytes, 0, 4);
        byte[] imageBytes = new byte[getIntegerFromBytes(lenBytes)];
        IOUtils.read(inputStream, imageBytes);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private int getIntegerFromBytes(byte[] lenBytes) {
        return ((lenBytes[3] & 0xff) << 24) | ((lenBytes[2] & 0xff) << 16) |
                ((lenBytes[1] & 0xff) << 8) | (lenBytes[0] & 0xff);
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
