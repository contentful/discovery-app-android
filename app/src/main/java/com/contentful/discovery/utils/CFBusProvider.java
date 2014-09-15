package com.contentful.discovery.utils;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Otto {@link com.squareup.otto.Bus} extension which allows calling {@link Bus#post(Object)}
 * from the context of any thread, while actually posting it on the main thread.
 */
public class CFBusProvider {
    private static CFBus sInstance = null;

    public static Bus getInstance() {
        if (sInstance == null) {
            sInstance = new CFBus();
        }

        return sInstance;
    }

    private static class CFBus extends Bus {
        private final Handler mainThread = new Handler(Looper.getMainLooper());

        public CFBus() {
            super(ThreadEnforcer.ANY);
        }

        @Override
        public void post(final Object event) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                super.post(event);
            } else {
                mainThread.post(new Runnable() {
                    @Override
                    public void run() {
                        CFBus.super.post(event);
                    }
                });
            }
        }
    }
}
