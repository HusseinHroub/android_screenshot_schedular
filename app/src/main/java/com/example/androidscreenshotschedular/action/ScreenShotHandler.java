package com.example.androidscreenshotschedular.action;

import android.widget.ImageView;

@FunctionalInterface
public interface ScreenShotHandler {
    void onClick(ImageView smallImage, String imagePath);
}
