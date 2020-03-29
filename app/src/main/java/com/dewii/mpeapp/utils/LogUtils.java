package com.dewii.mpeapp.utils;

import android.util.Log;

import com.android.volley.VolleyError;

public final class LogUtils {
    private static final String TAG = LogUtils.class.getCanonicalName();
    private static final boolean SHOULD_LOG = true;

    private LogUtils() {

    }



    public static void logInfo(String message) {
        logInfo(TAG, message);
    }

    public static void logInfo(String TAG, String message) {
        if (SHOULD_LOG)
            Log.d(TAG, "logInfo: " + message);
    }



    public static void logRequest(String message) {
        logRequest(TAG, message);
    }

    public static void logRequest(String TAG, String message) {
        if (SHOULD_LOG)
            Log.i(TAG, "logRequest: " + message);
    }



    public static void logResponse(String message) {
        logResponse(TAG, message);
    }

    public static void logResponse(String TAG, String message) {
        if (SHOULD_LOG)
            Log.i(TAG, "logResponse: " + message);
    }



    public static void logError(VolleyError volleyError) {
        logError(TAG, volleyError);
    }

    public static void logError(String message) {
        logError(TAG, message);
    }

    public static void logError(String tag, VolleyError volleyError) {
        logError(tag, volleyError.getLocalizedMessage());
    }

    public static void logError(String tag, String message) {
        if (SHOULD_LOG)
            Log.e(tag, "logError: " + message);
    }



    public static void println(String message) {
        println(TAG, message);
    }

    public static void println(String tag, String message) {
        if (SHOULD_LOG)
            System.out.println(tag + "println: " + message);
    }
}
