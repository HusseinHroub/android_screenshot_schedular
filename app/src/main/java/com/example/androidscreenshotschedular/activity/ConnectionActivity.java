package com.example.androidscreenshotschedular.activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.Constants;

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
//        SharedPreferences mySharedPrefrence = getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = mySharedPrefrence.edit();
//        editor.putString(Constants.SHARED_PREF_SERVER_IP_ADDRESS, "192.168.1.0");
//        editor.apply();


        connectInThread();

    }

    private void connectInThread() {
        attemptCounter = 0;
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (getServerIpAddress().isEmpty()) {
                    managePacketSendRead();
                }
            }
        }).start();
    }

    public void connect(View connectButton) {
        connectButton.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        searchingFeedBack.setText("Searching for server");
        connectInThread();
    }

    private void managePacketSendRead() {
        try {
            DatagramSocket socket = new DatagramSocket();
            sendToBroadCast(socket);
            String packetString = receivePacketAndGetString(socket);
        } catch (SocketTimeoutException e) {
            manageConnectAttempts();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void manageConnectAttempts() {
        if (++attemptCounter < MAXIMUM_ATTEMPTS_OF_CONNECT) {
            reTrySearch();
        } else {
            showFailedFindingServerToClient();
        }
    }

    private void showFailedFindingServerToClient() {
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
        managePacketSendRead();
    }

    private String receivePacketAndGetString(DatagramSocket socket) throws IOException {
        byte[] recBuffer = new byte[1024];
        DatagramPacket dataGramPacketRead = new DatagramPacket(recBuffer, recBuffer.length);
        socket.setSoTimeout(3000);
        socket.receive(dataGramPacketRead);
        socket.close();
        return new String(dataGramPacketRead.getData(), 0, dataGramPacketRead.getLength());

    }

    private void sendToBroadCast(DatagramSocket socket) throws IOException {
        byte[] buffer = Constants.WHO_IS_SERVER_SCHEDULER.getBytes();
        DatagramPacket packetToSend = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("192.168.1.255"), 8888);
        socket.send(packetToSend);
    }

    private String getServerIpAddress() {
        SharedPreferences sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        return sharedPreferences.getString(Constants.SHARED_PREF_SERVER_IP_ADDRESS, "");
    }
}
