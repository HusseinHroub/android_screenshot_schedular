package com.example.androidscreenshotschedular.service.factory;

import android.widget.TextView;
import com.example.androidscreenshotschedular.service.ScreenShotSchedulerService;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotSchedulerFactory {


    public static void startScreenSchedulerService(TimesConfiguration timesConfiguration, TextView feedBackView) {
        ScreenShotSchedulerService screenShotSchedulerService = null;
        screenShotSchedulerService.start(timesConfiguration, feedBackView);
    }

    private ScreenShotSchedulerFactory() {
    }
}
