package com.example.androidscreenshotschedular.service.factory;

import com.example.androidscreenshotschedular.action.ConnectionAcknowledgment;
import com.example.androidscreenshotschedular.service.SchedulerService;
import com.example.androidscreenshotschedular.service.tcp.RealSchedulerService;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class ScreenShotSchedulerFactory {

    private ScreenShotSchedulerFactory() {
    }

    private static SchedulerService schedulerService;

    public static void startScreenSchedulerService(TimesConfiguration timesConfiguration, ConnectionAcknowledgment connectionAcknowledgment) {
        closeProcessor();
        schedulerService = new RealSchedulerService();
        schedulerService.start(timesConfiguration, connectionAcknowledgment);
    }

    public static void closeProcessor() {
        if (schedulerService != null)
            schedulerService.close();
    }

}
