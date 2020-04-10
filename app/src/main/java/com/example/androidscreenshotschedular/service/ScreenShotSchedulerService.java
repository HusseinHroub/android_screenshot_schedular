package com.example.androidscreenshotschedular.service;

import android.widget.TextView;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public interface ScreenShotSchedulerService {
    void start(TimesConfiguration timesConfiguration, TextView feedBackView);
}
