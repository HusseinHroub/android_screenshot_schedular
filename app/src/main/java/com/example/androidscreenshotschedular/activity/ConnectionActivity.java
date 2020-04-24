package com.example.androidscreenshotschedular.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.service.udp.ServerSearchService;
import com.example.androidscreenshotschedular.utils.HelperUtil;

import java.net.SocketException;

public class ConnectionActivity extends AppCompatActivity {

    private TextView searchingFeedBack;
    private ProgressBar loading;
    private Button connectButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connection);
        init();
        manageConnection();

    }

    private void init() {
        searchingFeedBack = findViewById(R.id.searching_feedback_text_view);
        loading = findViewById(R.id.loading_progress_bar);
        connectButton = findViewById(R.id.connect_button);
    }

    private void manageConnection() {
        if (HelperUtil.getServerIpAddress(this).isEmpty()) {
            searchForServer();
        } else {
            startStarterActivity();
        }
    }

    private void searchForServer() {
        try {
            ServerSearchService serverSearchService = new ServerSearchService(this);
            initServerSearchServiceListeners(serverSearchService);
            serverSearchService.startSearchingInNewThread();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    private void initServerSearchServiceListeners(ServerSearchService serverSearchService) {
        initSearchFailListener(serverSearchService);
        initNewSearchAttemptListener(serverSearchService);
        initOnServerFoundListener(serverSearchService);
    }

    private void initNewSearchAttemptListener(ServerSearchService serverSearchService) {
        serverSearchService.setOnNewSearchAttemptListener(attemptCounter ->
                searchingFeedBack.setText(String.format("Re searching for server (%s)", attemptCounter)));
    }

    private void initSearchFailListener(ServerSearchService serverSearchService) {
        serverSearchService.setOnSearchFailListener(() -> {
            loading.setVisibility(View.GONE);
            searchingFeedBack.setText("Couldn't find server");
            connectButton.setEnabled(true);
        });
    }

    private void initOnServerFoundListener(ServerSearchService serverSearchService) {
        serverSearchService.setOnServerFoundListener(() -> startStarterActivity());
    }

    public void connectToServiceButtonAction(View connectButton) {
        connectButton.setEnabled(false);
        loading.setVisibility(View.VISIBLE);
        searchingFeedBack.setText("Searching for server");
        manageConnection();
    }


    private void startStarterActivity() {
        startActivity(new Intent(this, StartActivity.class));
        finish();
    }

}
