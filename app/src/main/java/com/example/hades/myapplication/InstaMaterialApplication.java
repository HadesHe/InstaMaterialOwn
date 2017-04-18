package com.example.hades.myapplication;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by Hades on 2017/4/4.
 */

public class InstaMaterialApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Timber.plant(new Timber.DebugTree());
    }
}
