package com.example.androidscreenshotschedular.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.HelperUtil;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

public class ConnectionActivity extends AppCompatActivity {

    private static final int MAXIMUM_ATTEMPTS_OF_CONNECT = 4;
    private int attemptCounter;
    private Handler handler;
    private TextView searchingFeedBack;
    private ProgressBar loading;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        handler = new Handler(Looper.getMainLooper());
        searchingFeedBack = findViewById(R.id.searching_feedback_text_view);
        loading = findViewById(R.id.loading_progress_bar);
        connectButton = findViewById(R.id.connect_button);
        manageConnection();

    }

    private void manageConnection() {
        if (HelperUtil.getServerIpAddress(this).isEmpty()) {
            findServerIpInNewThread();
        } else {
            startStarterActivity();
        }
    }

    private void findServerIpInNewThread() {
        attemptCounter = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                managePacketSendRec();
            }
        }).start();
    }

    public void connect(View connectButton) {
        connectButton.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        searchingFeedBack.setText("Searching for server");
        manageConnection();
    }

    private void managePacketSendRec() {
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(Constants.UDP_REC_PORT);
            sendPacketAndHandleRec(socket);
        } catch (SocketTimeoutException e) {
            socket.close();
            manageConnectAttempts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendPacketAndHandleRec(DatagramSocket socket) throws IOException {
        sendToBroadCast(socket);
        handleIfFromServer(receivePacketInfo(socket));
    }

    private void handleIfFromServer(PacketInfo packetInfo) {
        if (packetInfo.equals(Constants.ACK_CODE)) {
            HelperUtil.saveIpAddress(this, packetInfo.getIpAddress());
            startStarterActivity();
        }
    }

    private void startStarterActivity() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
        finish();
    }

    private void manageConnectAttempts() {
        if (++attemptCounter < MAXIMUM_ATTEMPTS_OF_CONNECT) {
            reTrySearch();
        } else {
            showFailedFindingServer();
        }
    }

    private void showFailedFindingServer() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                loading.setVisibility(View.GONE);
                searchingFeedBack.setText("Couldn't find server");
                connectButton.setEnabled(true);
            }
        });
    }

    private void reTrySearch() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                searchingFeedBack.setText(String.format("Re searching for server (%s)", attemptCounter));
            }
        });
        managePacketSendRec();
    }

    private PacketInfo receivePacketInfo(DatagramSocket socket) throws IOException {
        byte[] recBuffer = new byte[1024];
        DatagramPacket dataGramPacketRead = new DatagramPacket(recBuffer, recBuffer.length);
        socket.setSoTimeout(3000);
        socket.receive(dataGramPacketRead);
        return new PacketInfo(dataGramPacketRead);

    }

    private void sendToBroadCast(DatagramSocket socket) throws IOException {
        byte[] buffer = Constants.WHO_IS_SERVER_SCHEDULER.getBytes();
        DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length,
                InetAddress.getByName("192.168.1.255"), Constants.UDP_SEND_PORT);
        socket.send(packetToSend);
    }

    private class PacketInfo {
        private String packetContent;
        private String ipAddress;

        public PacketInfo(DatagramPacket datagramPacket) {
            this.packetContent = new String(datagramPacket.getData(),
                    0,
                    datagramPacket.getLength());
            this.ipAddress = datagramPacket.getAddress().toString();
        }

        public String getPacketContent() {
            return packetContent;
        }

        public String getIpAddress() {
            return ipAddress;
        }


        @Override
        public boolean equals(@Nullable Object obj) {
            return packetContent.equals(obj);
        }
    }

}
