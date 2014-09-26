package com.contentful.discovery.utils;

import com.contentful.discovery.CFApp;

/**
 * Intent Constants.
 */
public class IntentConsts {
    public static final String PACKAGE_NAME = CFApp.getInstance().getPackageName();

    public static final String EXTRA_SPACE = PACKAGE_NAME + ".EXTRA_SPACE";
    public static final String EXTRA_CREDENTIALS = PACKAGE_NAME + ".EXTRA_CREDENTIALS";
    public static final String EXTRA_CONTENT_TYPE = PACKAGE_NAME + ".EXTRA_CONTENT_TYPE";
    public static final String EXTRA_ENTRY = PACKAGE_NAME + ".EXTRA_ENTRY";
    public static final String EXTRA_ASSET = PACKAGE_NAME + ".EXTRA_ASSET";
    public static final String EXTRA_TEXT = PACKAGE_NAME + ".EXTRA_TEXT";
    public static final String EXTRA_LOCATION = PACKAGE_NAME + ".EXTRA_LOCATION";
    public static final String EXTRA_TITLE = PACKAGE_NAME + ".EXTRA_TITLE";
    public static final String EXTRA_LIST = PACKAGE_NAME + ".EXTRA_LIST";
    public static final String EXTRA_URL = PACKAGE_NAME + ".EXTRA_URL";
    public static final String EXTRA_ALLOW_LINKS = PACKAGE_NAME + ".EXTRA_ALLOW_LINKS";
    public static final String EXTRA_CONTENT_TYPES_MAP = PACKAGE_NAME + ".EXTRA_CONTENT_TYPES_MAP";

    /**
     * DBIntentService
     */
    public static class DB {
        public static final String ACTION_SAVE_CREDENTIALS = PACKAGE_NAME + ".ACTION_SAVE_CREDENTIALS";
    }

}
