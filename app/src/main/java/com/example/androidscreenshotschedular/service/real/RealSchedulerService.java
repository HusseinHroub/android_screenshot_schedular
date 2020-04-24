package com.example.androidscreenshotschedular.service.real;

import com.example.androidscreenshotschedular.action.ConnectionAcknowledgment;
import com.example.androidscreenshotschedular.service.SchedulerService;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class RealSchedulerService implements SchedulerService {
    private RealScreenShotProcessScheduler realScreenShotProcessScheduler;
    private ConnectionAcknowledgment connectionAcknowledgment;


    @Override
    public void start(TimesConfiguration timesConfiguration , ConnectionAcknowledgment connectionAcknowledgment) {
        this.connectionAcknowledgment = connectionAcknowledgment;
        takeScreenShotEach(getTimeInMillieSeconds(timesConfiguration));

    }

    @Override
    public void close() {
        stopProcessIfNotNull();
    }

    private void takeScreenShotEach(long timeInMillieSeconds) {
        stopProcessIfNotNull();
        realScreenShotProcessScheduler = new RealScreenShotProcessScheduler(timeInMillieSeconds, connectionAcknowledgment);
        realScreenShotProcessScheduler.start();
    }

    private void stopProcessIfNotNull() {
        if (realScreenShotProcessScheduler != null)
            realScreenShotProcessScheduler.stop();
    }

    private long getTimeInMillieSeconds(TimesConfiguration timesConfiguration) {
        return convertToMillieSeconds(timesConfiguration.getTimePeriod(), timesConfiguration.getTimeUnit());

    }

    private long convertToMillieSeconds(int timePeriod, int timeUnit) {
        long timeInMillieSecond = 0;
        if (timeUnit == Constants.INDEX_SECOND) {
            timeInMillieSecond = convertFromSecondToMillie(timePeriod);
        } else if (timeUnit == Constants.INDEX_MINUTE) {
            timeInMillieSecond = convertFromMinuteToMillie(timePeriod);
        } else if (timeUnit == Constants.INDEX_HOUR) {
            timeInMillieSecond = convertFromHourToMillie(timePeriod);
        }
        return timeInMillieSecond;
    }

    private int convertFromSecondToMillie(int timePeriod) {
        return timePeriod * 1000;
    }

    private int convertFromMinuteToMillie(int timePeriod) {
        return timePeriod * 60 * 1000;
    }

    private int convertFromHourToMillie(int timePeriod) {
        return timePeriod * 60 * 60 * 1000;
    }


}
