package com.example.androidscreenshotschedular.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.widget.GridView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.adapter.ImagesAdapter;
import com.example.androidscreenshotschedular.service.factory.ScreenShotSchedulerFactory;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

import java.io.File;

public class ScreenShotsInformationActivity extends AppCompatActivity {

    private TimesConfiguration timesConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shots_information);
        prepareTimesConfiguration();
        initializeGridViewImage();
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
        ScreenShotSchedulerFactory.startScreenSchedulerService(timesConfiguration, getTakenScreenShotsCounterTextView(), this);
    }

    private TextView getTakenScreenShotsCounterTextView() {
        return findViewById(R.id.taken_screen_shots_counter_text_view);
    }

    private void initializeGridViewImage() {
        GridView gridView = findViewById(R.id.image_grid_view);
        gridView.setAdapter(new ImagesAdapter(this, getScreenShotFiles()));
    }

    private File[] getScreenShotFiles() {
        File screenShotsDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.PC_SCREEN_SHOT_DIR);
        File[] listOfImages = screenShotsDir.listFiles();
        return listOfImages == null ? new File[]{} : listOfImages;
    }
}
