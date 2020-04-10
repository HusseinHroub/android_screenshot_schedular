package com.example.androidscreenshotschedular.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.service.factory.ScreenShotSchedulerFactory;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotsInformationActivity extends AppCompatActivity {

    private TimesConfiguration timesConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shots_information);
        prepareTimesConfiguration();
        startScreenShotSchedulerService();
    }

    private void prepareTimesConfiguration() {
        Intent fromStartActivityIntent = getIntent();
        timesConfiguration = new TimesConfiguration(getTimePeriodFromIntent(fromStartActivityIntent),
                getTimeUnitFromIntent(fromStartActivityIntent));
    }

    private int getTimeUnitFromIntent(Intent fromStartActivityIntent) {
        return fromStartActivityIntent.getIntExtra(Constants.INTENT_TIME_UNIT, 0);
    }

    private int getTimePeriodFromIntent(Intent fromStartActivityIntent) {
        return fromStartActivityIntent.getIntExtra(Constants.INTENT_TIME_PERIOD, 1);
    }

    private void startScreenShotSchedulerService() {
        ScreenShotSchedulerFactory.startScreenSchedulerService(timesConfiguration, getTakenScreenShotsTextView(), this);
    }

    private TextView getTakenScreenShotsTextView() {
        return findViewById(R.id.taken_screen_shots_text_view);
    }
}
