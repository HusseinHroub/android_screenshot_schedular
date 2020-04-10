package com.example.androidscreenshotschedular.service.factory;

import android.widget.TextView;
import com.example.androidscreenshotschedular.service.fake.FakeSchedulerService;
import com.example.androidscreenshotschedular.service.ScreenShotSchedulerService;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotSchedulerFactory {


    public static void startScreenSchedulerService(TimesConfiguration timesConfiguration, TextView feedBackView) {
        ScreenShotSchedulerService screenShotSchedulerService = new FakeSchedulerService();
        screenShotSchedulerService.start(timesConfiguration, feedBackView);
    }

    private ScreenShotSchedulerFactory() {
    }
}
