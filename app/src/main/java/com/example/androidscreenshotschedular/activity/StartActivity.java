package com.example.androidscreenshotschedular.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.Constants;

public class StartActivity extends AppCompatActivity {

    private Button startScreenShotSchedulerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        initializeViews();
        initializeTimeUnitsDataInSpinner();
        checkAndManageStoragePermission();
    }

    private void initializeViews() {
        startScreenShotSchedulerButton = findViewById(R.id.start_screen_shot_sechedular_button);
    }

    private void initializeTimeUnitsDataInSpinner() {
        Spinner spinner = getTimeUnitSpinner();
        ArrayAdapter<CharSequence> adapter = createAdapterWithDefaultLayout();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }


    private ArrayAdapter<CharSequence> createAdapterWithDefaultLayout() {
        return ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
    }

    public void startScreenShotInformationActivity(View startScreenShotSchedulerButton) {
        startActivity(prepareAndGetToScreenShotIntent());
    }

    private Intent prepareAndGetToScreenShotIntent() {
        Intent toScreenShotInformationActivity = new Intent(this, ScreenShotsInformationActivity.class);
        toScreenShotInformationActivity.putExtra(Constants.INTENT_TIME_PERIOD, getTimePeriodValue());
        toScreenShotInformationActivity.putExtra(Constants.INTENT_TIME_UNIT, getTimeUnitValue());
        return toScreenShotInformationActivity;
    }

    private int getTimePeriodValue() {
        EditText timePeriodEditText = getTimePeriodEditText();
        return Integer.parseInt(timePeriodEditText.getText().toString());
    }

    private int getTimeUnitValue() {
        Spinner spinner = getTimeUnitSpinner();
        return spinner.getSelectedItemPosition();
    }

    private Spinner getTimeUnitSpinner() {
        return findViewById(R.id.time_unit_spinner);
    }


    private EditText getTimePeriodEditText() {
        return findViewById(R.id.time_period_edit_text);
    }

    private void checkAndManageStoragePermission() {
        if (isPermissionNotGranted()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        } else {
            enableScreenShotScheduleButton();
        }
    }


    private boolean isPermissionNotGranted() {
        return ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (isWriteExternalStoragePermissionGranted(requestCode, grantResults)) {
            enableScreenShotScheduleButton();
        } else {
            showToastForNotHavingPermission();
        }

    }

    private void enableScreenShotScheduleButton() {
        startScreenShotSchedulerButton.setEnabled(true);
    }

    private void showToastForNotHavingPermission() {
        Toast.makeText(this, "I must have an access to hac I mean save* your PC images into your gallery.", Toast.LENGTH_LONG).show();
    }

    private boolean isWriteExternalStoragePermissionGranted(int requestCode, int[] grantResults) {
        return (requestCode == 1) && (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }


}
