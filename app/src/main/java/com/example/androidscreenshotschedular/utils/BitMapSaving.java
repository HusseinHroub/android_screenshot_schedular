package com.example.androidscreenshotschedular.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitMapSaving {
    private static final String APP_DIR_NAME = "Pc ScreenShots";
    private static int seqCounter = 1;

    public static void saveBitMap(Bitmap bitmap) {
        File rootSavingPath = makeAndGetRootSavingPath();
        File ourImage = new File(rootSavingPath, String.format("image%d.png", seqCounter++));
        writeBitMapToOurImage(bitmap, ourImage);
    }

    private static File makeAndGetRootSavingPath() {
        File rootSavingPath = getRootSavingPath(APP_DIR_NAME);
        if (!rootSavingPath.exists())
            rootSavingPath.mkdir();
        return rootSavingPath;
    }

    private static void writeBitMapToOurImage(Bitmap ourTestImageBitMap, File image) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(image);
            ourTestImageBitMap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static File getRootSavingPath(String appDirectoryName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);
    }
}
