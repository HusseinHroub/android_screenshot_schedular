package com.example.androidscreenshotschedular.service.udp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.HelperUtil;
import com.example.androidscreenshotschedular.utils.PacketInfo;

import java.io.IOException;
import java.net.*;

public class ServerSearchService {

    private static final int MAXIMUM_ATTEMPTS_OF_CONNECT = 4;
    private static final int TIME_OUT_IN_MILLIS = 3000;

    private int attemptCounter;
    private Runnable failedSearchServerRunnable;
    private Search searchHandler;
    private Found foundServerHandler;
    private final Context context;
    private DatagramSocket socket;

    public ServerSearchService(Context context) throws SocketException {
        this.context = context;
        socket = new DatagramSocket(Constants.UDP_REC_PORT);
    }

    public void startSearchingInNewThread() {
        attemptCounter = 0;
        new Thread(() -> {
            managePacketSendRec();
            socket.close();
            HelperUtil.printLog("Okay closed socket of UDP!!!!!");
        }).start();
    }

    private void managePacketSendRec() {
        try {
            sendPacketAndHandleRec(socket);
        } catch (SocketTimeoutException e) {
            manageConnectAttempts();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void sendPacketAndHandleRec(DatagramSocket socket) throws IOException {
        sendToBroadCast(socket);
        handleIfFromServer(receivePacketInfo(socket));
    }

    private void sendToBroadCast(DatagramSocket socket) throws IOException {
        byte[] buffer = Constants.WHO_IS_SERVER_SCHEDULER.getBytes();
        DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length,
                InetAddress.getByName("192.168.1.255"), Constants.UDP_SEND_PORT);
        socket.send(packetToSend);
    }

    //TODO handle if not from server!
    private void handleIfFromServer(PacketInfo packetInfo) {
        if (packetInfo.equals(Constants.ACK_CODE)) {
            HelperUtil.saveIpAddress(context, packetInfo.getIpAddress());
            handleServerFound();
        }
    }

    private void handleServerFound() {
        if (foundServerHandler != null) {
            foundServerHandler.onServerFound();
        }
    }

    private void manageConnectAttempts() {
        if (++attemptCounter < MAXIMUM_ATTEMPTS_OF_CONNECT) {
            reTrySearch();
        } else {
            handleFailedFindingServer();
        }
    }

    private void reTrySearch() {
        handleAttemptsCounter();
        managePacketSendRec();
    }

    private void handleAttemptsCounter() {
        if (searchHandler != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> searchHandler.onReTryAttempt(attemptCounter));
        }

    }

    private void handleFailedFindingServer() {
        if (failedSearchServerRunnable != null) {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(failedSearchServerRunnable);
        }
    }

    public void setOnServerFoundListener(Found foundServerHandler) {
        this.foundServerHandler = foundServerHandler;

    }

    private PacketInfo receivePacketInfo(DatagramSocket socket) throws IOException {
        byte[] recBuffer = new byte[1024];
        DatagramPacket dataGramPacketRead = new DatagramPacket(recBuffer, recBuffer.length);
        socket.setSoTimeout(TIME_OUT_IN_MILLIS);
        socket.receive(dataGramPacketRead);
        return new PacketInfo(dataGramPacketRead);

    }

    public void setOnSearchFailListener(Runnable searchFindingServerRunnable) {
        this.failedSearchServerRunnable = searchFindingServerRunnable;
    }

    public void setOnNewSearchAttemptListener(Search searchHandler) {
        this.searchHandler = searchHandler;
    }

    public interface Search {
        void onReTryAttempt(int attemptCounter);
    }

    public interface Found {
        void onServerFound();
    }
}
