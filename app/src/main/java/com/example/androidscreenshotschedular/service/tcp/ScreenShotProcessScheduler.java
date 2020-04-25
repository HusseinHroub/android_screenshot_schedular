package com.example.androidscreenshotschedular.service.tcp;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

import com.example.androidscreenshotschedular.action.ConnectionAcknowledgment;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.HelperUtil;
import com.example.androidscreenshotschedular.utils.TasksHandler;
import com.example.androidscreenshotschedular.utils.bitmap.BitMapSaving;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

public class ScreenShotProcessScheduler {
    private static final int CONNECTION_TIME_OUT = 4000;
    private long timeInMilliSeconds;
    private HandlerThread handlerThread;
    private ConnectionAcknowledgment connectionAcknowledgment;
    private Handler mainUiHandler;
    private Socket clientSocket;
    private TasksHandler postTasksHandler;

    public ScreenShotProcessScheduler(long timeInMilliSecond, ConnectionAcknowledgment connectionAcknowledgment) {
        this.timeInMilliSeconds = timeInMilliSecond;
        this.connectionAcknowledgment = connectionAcknowledgment;
        mainUiHandler = new Handler(Looper.getMainLooper());
        postTasksHandler = new TasksHandler(startAndGetBackGroundHandlerThread().getLooper());

    }

    public void start() {
        postTasksHandler.postTask(getConnectToServerTask());
        postTasksHandler.postTaskEach(getProcessTask(), timeInMilliSeconds);
    }

    private HandlerThread startAndGetBackGroundHandlerThread() {
        HelperUtil.printLog("RealScreenShotProcessScheduler.stop thread initialized");//TODO remove
        handlerThread = new HandlerThread("ImageSchedulerHandler");
        handlerThread.start();
        return handlerThread;
    }


    private Runnable getConnectToServerTask() {
        return () -> {
            try {
                connectToServer();
                HelperUtil.printLog("RealScreenShotProcessScheduler.run: Connected to server");//TODO remove
            } catch (IOException e) {
                e.printStackTrace();
                manageFailConnection();
            }
        };
    }


    private void connectToServer() throws IOException {
        clientSocket = new Socket();
        clientSocket.connect(new InetSocketAddress(HelperUtil.getServerIpAddress(connectionAcknowledgment.getContext()),
                Constants.TCP_PORT), CONNECTION_TIME_OUT);
    }

    private Runnable getProcessTask() {
        return () -> {
            try {
                HelperUtil.printLog("RealScreenShotProcessScheduler.run: Okay so posting task each");//TODO remove
                sendRequestForScreenShot();
                BitMapSaving.saveBitMap(getPcScreenShotBitMap(), (this::sendTakenScreenShotFeedBack));
            } catch (IOException e) {
                e.printStackTrace();
                manageFailConnection();
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

    private void manageFailConnection() {
        mainUiHandler.post(() -> connectionAcknowledgment.onConnectionFail());
        stop();
        postTasksHandler.stopRepetitiveTask();

    }

    public void stop() {
        stopHandlerThread();
        closeClientSocket();
    }

    private void stopHandlerThread() {
        if (handlerThread != null) {
            handlerThread.quit();
            handlerThread = null;
            HelperUtil.printLog("RealScreenShotProcessScheduler.stop thread stopped");//TODO remove
        }
    }

    private void closeClientSocket() {
        if (clientSocket != null) {
            try {
                clientSocket.close();
                clientSocket = null;
                HelperUtil.printLog("RealScreenShotProcessScheduler.stop closed client socket");//TODO remove
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void sendTakenScreenShotFeedBack(File bitMapFile) {
        mainUiHandler.post(() -> connectionAcknowledgment.onScreenShotTaken(bitMapFile));
    }

}
