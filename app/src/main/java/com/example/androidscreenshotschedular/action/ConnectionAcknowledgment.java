package com.example.androidscreenshotschedular.action;

import android.content.Context;

import java.io.File;

public interface ConnectionAcknowledgment {
    Context getContext();

    void onConnectionFail();

    void onScreenShotTaken(File imageFile);
}
