package com.dewii.mpeapp.utils;

import android.graphics.Color;

public final class NumericUtils {
    public static final String TAG = NumericUtils.class.getCanonicalName();

    private NumericUtils() {

    }

    public static int parseInt(String value) {
        try {
            return Integer.parseInt(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static float parseFloat(String value) {
        try {
            return Float.parseFloat(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (float) 0;
        }
    }

    public static long parseLong(String value) {
        try {
            return Long.parseLong(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (long) 0;
        }
    }

    public static double parseDouble(String value) {
        try {
            return Double.parseDouble(value);
        } catch (Exception e) {
            e.printStackTrace();
            return (double) 0;
        }
    }

    public static int parseColor(String value) {
        try {
            return Color.parseColor(value);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static boolean toBoolean(int value) {
        return value == 1;
    }

    public static boolean isGreaterInDoubles(String s1, String s2) {
        return isGreaterInDoubles(parseDouble(s1), parseDouble(s2));
    }

    public static boolean isGreaterInDoubles(double d1, double d2) {
        return (d1 > d2);
    }

    public static boolean isInvalidDouble(Double d) {
        return d == null || d.isNaN() || d.isInfinite();
    }

    public static double makeNonNaN(double value) {
        if (Double.valueOf(value).isNaN())
            return 0;
        else
            return value;
    }

    public static double roundOff(double num)
    {
        return Math.round(num * 100.0) / 100.0;
    }

    public static int toInt(boolean value) {
        return value ? 1 : 0;
    }
}
