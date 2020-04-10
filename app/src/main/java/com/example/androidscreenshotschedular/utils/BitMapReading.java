package com.example.androidscreenshotschedular.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class BitMapReading {

    private int desiredImageHeight;
    private int desiredImageWidth;

    public BitMapReading(int desiredImageHeight, int desiredImageWidth) {
        this.desiredImageHeight = desiredImageHeight;
        this.desiredImageWidth = desiredImageWidth;
    }

    public Bitmap decodeImageFromLocation(String path) {
        BitmapFactory.Options bitMapFactoryOptions = queryWidthAndHeightToOption(path);
        bitMapFactoryOptions.inSampleSize = calculateInSampleSize(bitMapFactoryOptions);
        bitMapFactoryOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path, bitMapFactoryOptions);
    }

    private BitmapFactory.Options queryWidthAndHeightToOption(String path) {
        BitmapFactory.Options bitMapFactoryOptions = new BitmapFactory.Options();
        bitMapFactoryOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, bitMapFactoryOptions);
        return bitMapFactoryOptions;
    }

    private int calculateInSampleSize(BitmapFactory.Options bitMapOptions) {
        int height = bitMapOptions.outHeight;
        int width = bitMapOptions.outWidth;
        int inSampleSize = 1;

        if (height > desiredImageHeight || width > desiredImageWidth) {
            inSampleSize = getInSampleSize(height, width);
        }

        return inSampleSize;
    }

    private int getInSampleSize(int height, int width) {
        return width > height ?
                Math.round((float) height / (float) desiredImageHeight) :
                Math.round((float) width / (float) desiredImageWidth);
    }
}
