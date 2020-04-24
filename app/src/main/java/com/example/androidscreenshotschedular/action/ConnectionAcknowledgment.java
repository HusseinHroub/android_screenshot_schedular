package com.example.androidscreenshotschedular.action;

import android.content.Context;

public interface ConnectionAcknowledgment {
    Context getContext();

    void onConnectionFail();

    void onScreenShotTaken(int numberOfScreenShots);
}
