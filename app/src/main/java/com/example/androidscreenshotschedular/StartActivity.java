package com.example.androidscreenshotschedular;

import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_screen);
        initializeTimeUnitsDataInSpinner();
    }

    private void initializeTimeUnitsDataInSpinner() {
        Spinner spinner = findViewById(R.id.time_unit_spinner);
        ArrayAdapter<CharSequence> adapter = createAdapterWithDefaultLayout();
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private ArrayAdapter<CharSequence> createAdapterWithDefaultLayout() {
        return ArrayAdapter.createFromResource(this,
                R.array.time_units, android.R.layout.simple_spinner_item);
    }

    public void startScreenShotScheduler(View startScreenShotSchedulerButton)
    {

    }
}
