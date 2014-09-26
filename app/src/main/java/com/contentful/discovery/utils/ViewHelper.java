package com.contentful.discovery.utils;

import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * View Helper.
 */
public class ViewHelper {
    /**
     * Register a callback to be invoked when the global layout state or the visibility of views
     * within the view tree changes.
     *
     * @param v        The View.
     * @param listener The callback to add.
     * @see #removeGlobalLayoutListener(View, ViewTreeObserver.OnGlobalLayoutListener).
     */
    public static void addGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        ViewTreeObserver observer = v.getViewTreeObserver();

        if (observer == null || !observer.isAlive()) {
            return;
        }

        observer.addOnGlobalLayoutListener(listener);
    }

    /**
     * Remove a previously installed global layout callback.
     *
     * @param v        The View.
     * @param listener The callback to remove.
     * @see #addGlobalLayoutListener(View, ViewTreeObserver.OnGlobalLayoutListener).
     */
    @SuppressWarnings("deprecation")
    public static void removeGlobalLayoutListener(View v, ViewTreeObserver.OnGlobalLayoutListener listener) {
        ViewTreeObserver observer = v.getViewTreeObserver();
        if (observer == null || !observer.isAlive()) {
            return;
        }

        if (Build.VERSION.SDK_INT < 16) {
            observer.removeGlobalOnLayoutListener(listener);
        } else {
            observer.removeOnGlobalLayoutListener(listener);
        }
    }
}
