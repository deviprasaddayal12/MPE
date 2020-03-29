package com.dewii.mpeapp;

import android.app.Application;
import android.content.Context;

import com.squareup.picasso.Picasso;

public class MyApplication extends Application {
    public static final String TAG = MyApplication.class.getCanonicalName();

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        Picasso picasso = new Picasso.Builder(this).build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);

        Picasso.setSingletonInstance(picasso);
    }

    public static Context getContext() {
        return context;
    }
}
