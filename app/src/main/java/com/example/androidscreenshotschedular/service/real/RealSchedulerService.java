package com.example.androidscreenshotschedular.service.real;

import android.content.Context;
import android.widget.TextView;
import com.example.androidscreenshotschedular.service.SchedulerService;
import com.example.androidscreenshotschedular.utils.Constants;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public class RealSchedulerService implements SchedulerService {
    private TextView feedBackView;
    private Context context;


    @Override
    public void start(TimesConfiguration timesConfiguration, TextView feedBackView, Context context) {
        this.feedBackView = feedBackView;
        this.context = context;
        takeScreenShotEach(getTimeInMillieSeconds(timesConfiguration));

    }

    private void takeScreenShotEach(long timeInMillieSeconds) {
        RealScreenShotProcessScheduler realScreenShotProcessScheduler = new RealScreenShotProcessScheduler(timeInMillieSeconds, feedBackView, context);
        realScreenShotProcessScheduler.start();
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
