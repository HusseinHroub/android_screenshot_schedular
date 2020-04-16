package com.example.androidscreenshotschedular.service.factory;

import android.content.Context;
import android.widget.TextView;
import com.example.androidscreenshotschedular.service.SchedulerService;
import com.example.androidscreenshotschedular.service.real.RealSchedulerService;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotSchedulerFactory {


    public static void startScreenSchedulerService(TimesConfiguration timesConfiguration, TextView feedBackView, Context context) {
        SchedulerService schedulerService = new RealSchedulerService();
        schedulerService.start(timesConfiguration, feedBackView, context);
    }

    private ScreenShotSchedulerFactory() {
    }
}
