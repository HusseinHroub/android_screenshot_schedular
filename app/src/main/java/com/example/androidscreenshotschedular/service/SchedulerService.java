package com.example.androidscreenshotschedular.service;

import com.example.androidscreenshotschedular.action.ConnectionAcknowledgment;
import com.example.androidscreenshotschedular.utils.TimesConfiguration;

public interface SchedulerService {
    void start(TimesConfiguration timesConfiguration, ConnectionAcknowledgment connectionAcknowledgment);

    void close();
}
