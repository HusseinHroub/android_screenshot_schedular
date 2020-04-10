package com.example.androidscreenshotschedular.utils;

public class TimesConfiguration {
    private int timePeriod;
    private int timeUnit;

    public TimesConfiguration(int timeValue, int timeUnit) {
        this.timePeriod = timeValue;
        this.timeUnit = timeUnit;
    }

    public int getTimePeriod() {
        return timePeriod;
    }

    public int getTimeUnit() {
        return timeUnit;
    }

}
