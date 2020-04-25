package com.example.androidscreenshotschedular.action;

import java.io.File;

@FunctionalInterface
public interface BitMapAcknowledgment {
    void onBitMapSaved(File bitMapFile);
}
