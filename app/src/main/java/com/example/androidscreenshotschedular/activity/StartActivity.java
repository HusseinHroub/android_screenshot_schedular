package com.example.androidscreenshotschedular.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.example.androidscreenshotschedular.R;
import com.example.androidscreenshotschedular.utils.Constants;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);
        initializeTimeUnitsDataInSpinner();
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

    private int getTimeUnitValue() {
        Spinner spinner = getTimeUnitSpinner();
        return spinner.getSelectedItemPosition();
    }

    private Spinner getTimeUnitSpinner() {
        return findViewById(R.id.time_unit_spinner);
    }

    private int getTimePeriodValue() {
        EditText timePeriodEditText = getTimePeriodEditText();
        return Integer.parseInt(timePeriodEditText.getText().toString());
    }

    private EditText getTimePeriodEditText() {
        return findViewById(R.id.time_period_edit_text);
    }


}
