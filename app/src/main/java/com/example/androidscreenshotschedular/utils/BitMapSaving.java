package com.example.androidscreenshotschedular.utils;

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitMapSaving {
    private static int seqCounter = 1;

    public static void saveBitMap(Bitmap bitmap) {
        File rootSavingPath = makeAndGetRootSavingPath();
        File ourImage = new File(rootSavingPath, String.format("image%d.png", seqCounter++));
        writeBitMapToOurImage(bitmap, ourImage);
    }

    private static File makeAndGetRootSavingPath() {
        File rootSavingPath = getRootSavingPath(Constants.PC_SCREEN_SHOT_DIR);
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

    private static File getRootSavingPath(String appDirectoryName) {
        return new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), appDirectoryName);
    }
}
