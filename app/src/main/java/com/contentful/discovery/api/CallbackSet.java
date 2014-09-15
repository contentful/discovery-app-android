package com.contentful.discovery.api;

import com.contentful.java.api.CDACallback;

import java.util.HashSet;

/**
 * Callback Set.
 * Convenience class which holds a {@code Set} of {@link com.contentful.java.api.CDACallback}
 * instances and allows easy cancelling of the entire {@code Set}.
 */
public class CallbackSet {
    private HashSet<CDACallback> data;

    public CallbackSet() {
        this.data = new HashSet<CDACallback>();
    }

    public <T extends CDACallback> T addObject(T object) {
        data.add(object);
        return object;
    }

    public synchronized void cancelAndClear() {
        for (CDACallback cb : data) {
            cb.cancel();
        }

        data.clear();
    }
}
