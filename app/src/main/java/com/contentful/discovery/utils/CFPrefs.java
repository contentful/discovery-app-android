package com.contentful.discovery.utils;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.contentful.discovery.CFApp;

/**
 * Shared Preferences wrapper.
 */
public class CFPrefs {
    public static final String KEY_SPACE = "SPACE";
    public static final String KEY_ACCESS_TOKEN = "TOKEN";
    public static final String KEY_DID_LOGIN = "DID_LOGIN";

    public static SharedPreferences getInstance() {
        return PreferenceManager.getDefaultSharedPreferences(CFApp.getInstance());
    }

    public static Boolean didLogin() {
        return getInstance().getBoolean(KEY_DID_LOGIN, false);
    }
}
