package com.example.androidscreenshotschedular.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.action.ConnectionAcknowledgment;
import com.example.androidscreenshotschedular.adapter.ScreenShotAdapter;
import com.example.androidscreenshotschedular.animation.ZoomInAnimator;
import com.example.androidscreenshotschedular.service.factory.ScreenShotSchedulerFactory;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.HelperUtil;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;
import com.example.androidscreenshotschedular.utils.bitmap.BitMapReading;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ScreenShotsInformationActivity extends AppCompatActivity
        implements ConnectionAcknowledgment {

    private TimesConfiguration timesConfiguration;
    private TextView takenScreenShotTextView;
    private int counter;
    private ScreenShotAdapter screenShotAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_screen_shots_information);
        takenScreenShotTextView = getTakenScreenShotsCounterTextView();
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
        ScreenShotSchedulerFactory.startScreenSchedulerService(timesConfiguration, this);
    }

    private TextView getTakenScreenShotsCounterTextView() {
        return findViewById(R.id.taken_screen_shots_counter_text_view);
    }

    private void initializeGridViewImage() {
        GridView gridView = findViewById(R.id.image_grid_view);
        screenShotAdapter = new ScreenShotAdapter(this, getScreenShotPaths(), this::onScreenShotClick);
        gridView.setAdapter(screenShotAdapter);


    }

    public void onScreenShotClick(ImageView screenShot, String screenShotPath) {
        View container = findViewById(R.id.container);
        ImageView fullSizeScreenShot = findViewById(R.id.full_size_screenshot_image);
        DisplayMetrics displayMetrics = getDisplayPhoneScreenMetrics();
        fullSizeScreenShot.setImageBitmap(new BitMapReading(displayMetrics.heightPixels,
                displayMetrics.widthPixels).decodeImageFromLocation(screenShotPath));
        new ZoomInAnimator(screenShot, fullSizeScreenShot, container, this).zoomInToBiggerImage();
    }

    private DisplayMetrics getDisplayPhoneScreenMetrics() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }


    private List<String> getScreenShotPaths() {
        File screenShotsDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.PC_SCREEN_SHOT_DIR);
        File[] listOfScreenShotFiles = screenShotsDir.listFiles();
        List<String> screenShotPaths = new ArrayList<>();
        if (listOfScreenShotFiles != null) {
            for (File screenShotFile : listOfScreenShotFiles) {
                screenShotPaths.add(screenShotFile.getAbsolutePath());
            }
        }
        return screenShotPaths;
    }

    @Override
    protected void onDestroy() {
        HelperUtil.printLog("ScreenShotsInformationActivity.destroy");
        ScreenShotSchedulerFactory.closeProcessor();
        super.onDestroy();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void onConnectionFail() {
        Toast.makeText(this, "Lost connection to server!", Toast.LENGTH_SHORT).show();
        HelperUtil.saveIpAddress(this, "");
        startConnectionActivity();
    }

    @Override
    public void onScreenShotTaken(File imageFile) {
        screenShotAdapter.addImageAbsolutePath(imageFile.getAbsolutePath());
        counter++;
        takenScreenShotTextView.setText("" + counter);
    }

    private void startConnectionActivity() {
        startActivity(new Intent(this, ConnectionActivity.class));
        finishAffinity();
    }
}
