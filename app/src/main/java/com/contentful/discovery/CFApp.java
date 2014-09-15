package com.contentful.discovery;

import android.app.Application;

/**
 * Application Class.
 */
public class CFApp extends Application {
    public static CFApp sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        sInstance = this;
    }

    public static CFApp getInstance() {
        return sInstance;
    }
}
