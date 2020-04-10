package com.example.androidscreenshotschedular.service.fake;

import android.widget.TextView;
import com.example.androidscreenshotschedular.service.ScreenShotSchedulerService;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class FakeSchedulerService implements ScreenShotSchedulerService {
    private TextView feedBackView;

    @Override
    public void start(TimesConfiguration timesConfiguration, TextView feedBackView) {
        this.feedBackView = feedBackView;
        takeScreenShotEach(getTimeInMillieSeconds(timesConfiguration));

    }

    private void takeScreenShotEach(long timeInMillieSeconds) {
        ScreenShotProcessScheduler screenShotProcessScheduler = new ScreenShotProcessScheduler(timeInMillieSeconds, feedBackView);
        screenShotProcessScheduler.start();
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

    private int convertFromHourToMillie(int timePeriod) {
        return timePeriod * 60 * 60 * 1000;
    }

    private int convertFromMinuteToMillie(int timePeriod) {
        return timePeriod * 60 * 1000;
    }

    private int convertFromSecondToMillie(int timePeriod) {
        return timePeriod * 1000;
    }
}
