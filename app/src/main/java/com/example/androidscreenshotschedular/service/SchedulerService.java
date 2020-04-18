package com.example.androidscreenshotschedular.service;

import android.content.Context;
import android.widget.TextView;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public interface SchedulerService {
    void start(TimesConfiguration timesConfiguration, TextView feedBackView, Context context);
    void close();
}
