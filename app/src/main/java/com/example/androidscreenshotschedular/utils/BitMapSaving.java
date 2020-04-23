package com.example.androidscreenshotschedular.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class BitMapSaving {

    public static void saveBitMap(Bitmap bitmap) {
        File rootSavingPath = makeAndGetRootSavingPath();
        File ourImage = new File(rootSavingPath, getImageNameBasedOnDate());
        writeBitMapToOurImage(bitmap, ourImage);
    }

    private static String getImageNameBasedOnDate() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy - HH:mm:ss");
        return String.format("ss%s.png", simpleDateFormat.format(new Date()));
    }

    private static File makeAndGetRootSavingPath() {
        File rootSavingPath = getRootSavingPath();
        if (!rootSavingPath.exists())
            rootSavingPath.mkdir();
        return rootSavingPath;
    }

    private static void writeBitMapToOurImage(Bitmap ourTestImageBitMap, File image) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(image);
            ourTestImageBitMap.compress(Bitmap.CompressFormat.PNG, 85, fileOutputStream);
            cleanOutPutStream(fileOutputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cleanOutPutStream(FileOutputStream fileOutputStream) throws IOException {
        fileOutputStream.flush();
        fileOutputStream.close();
    }

    private static File getRootSavingPath() {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), Constants.PC_SCREEN_SHOT_DIR);
    }
}
