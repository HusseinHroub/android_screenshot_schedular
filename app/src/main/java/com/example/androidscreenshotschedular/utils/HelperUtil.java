package com.example.androidscreenshotschedular.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class HelperUtil {

    private static final boolean IS_LOG_ENABLED = true;
    private static String cachedIpAddress = "";

    public static float dpToPx(Context context, float dp) {
        return dp * context.getResources().getDisplayMetrics().density;
    }

    public static String getServerIpAddress(Context context) {
        if (cachedIpAddress.isEmpty()) {
            SharedPreferences sharedPreferences = getSharedPref(context);
            cachedIpAddress = sharedPreferences.getString(Constants.SHARED_PREF_SERVER_IP_ADDRESS, "");
        }
        return cachedIpAddress;
    }

    public static void saveIpAddress(Context context, String ipAddress) {
        if (!ipAddress.isEmpty()) {
            cachedIpAddress = ipAddress.substring(1);
        } else {
            cachedIpAddress = ipAddress;
        }
        SharedPreferences mySharedPreference = getSharedPref(context);
        SharedPreferences.Editor editor = mySharedPreference.edit();
        editor.putString(Constants.SHARED_PREF_SERVER_IP_ADDRESS, cachedIpAddress);
        editor.apply();
    }

    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void printLog(String message) {
        if (IS_LOG_ENABLED) {
            System.out.println(message);
        }

    }

}
