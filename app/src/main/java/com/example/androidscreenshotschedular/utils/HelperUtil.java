package com.example.androidscreenshotschedular.utils;

import android.content.Context;

public class HelperUtil {
    public static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }
}
