package com.example.androidscreenshotschedular.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class HelperUtil {

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
        cachedIpAddress = ipAddress.substring(1);
        SharedPreferences mySharedPreference = getSharedPref(context);
        SharedPreferences.Editor editor = mySharedPreference.edit();
        editor.putString(Constants.SHARED_PREF_SERVER_IP_ADDRESS, cachedIpAddress);
        editor.apply();
    }

    public static SharedPreferences getSharedPref(Context context) {
        return context.getSharedPreferences(Constants.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

}
