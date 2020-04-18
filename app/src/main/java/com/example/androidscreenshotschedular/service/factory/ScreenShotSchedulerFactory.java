package com.example.androidscreenshotschedular.service.factory;

import android.content.Context;
import android.widget.TextView;

import com.example.androidscreenshotschedular.service.SchedulerService;
import com.example.androidscreenshotschedular.service.real.RealSchedulerService;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotSchedulerFactory {

    private static SchedulerService schedulerService;

    public static void startScreenSchedulerService(TimesConfiguration timesConfiguration, TextView feedBackView, Context context) {
        closeProcessor();
        schedulerService = new RealSchedulerService();
        schedulerService.start(timesConfiguration, feedBackView, context);
    }

    private ScreenShotSchedulerFactory() {
    }

    public static void closeProcessor() {
        if (schedulerService != null)
            schedulerService.close();
    }
}
