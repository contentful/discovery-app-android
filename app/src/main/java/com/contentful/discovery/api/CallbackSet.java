package com.contentful.discovery.api;

import com.contentful.java.api.CDACallback;
import java.util.HashMap;
import java.util.HashSet;

/**
 * CallbackSet.
 * A thread-safe container of {@link com.contentful.java.api.CDACallback} instances.
 */
public class CallbackSet {
  private final HashSet<CDACallback> data;
  private final HashMap<String, HashSet<CDACallback>> tags;
  private final Object lock = new Object();

  public CallbackSet() {
    this.data = new HashSet<CDACallback>();
    this.tags = new HashMap<String, HashSet<CDACallback>>();
  }

  public <T extends CDACallback> T add(T callback) {
    synchronized (lock) {
      data.add(callback);
    }

    return callback;
  }

  public <T extends CDACallback> T add(String tag, T callback) {
    synchronized (lock) {
      HashSet<CDACallback> set = tags.get(tag);

      if (set == null) {
        set = new HashSet<CDACallback>();
        tags.put(tag, set);
      }

      set.add(callback);
      data.add(callback);
    }

    return callback;
  }

  public void cancelAndClear() {
    synchronized (lock) {
      for (CDACallback cb : data) {
        cb.cancel();
      }

      data.clear();
      tags.clear();
    }
  }

  public void cancel(String tag) {
    synchronized (lock) {
      HashSet<CDACallback> set = tags.get(tag);

      if (set != null) {
        for (CDACallback callback : set) {
          callback.cancel();
        }

        data.removeAll(set);
        set.clear();
      }

      tags.remove(tag);
    }
  }
}