package com.contentful.discovery.loaders;

import android.support.v4.content.AsyncTaskLoader;

import com.contentful.discovery.CFApp;

/**
 * {@link android.content.AsyncTaskLoader} base implementation.
 */
abstract class AbsAsyncTaskLoader<T> extends AsyncTaskLoader<T> {
    protected T result;

    public AbsAsyncTaskLoader() {
        super(CFApp.getInstance());
    }

    @Override
    public T loadInBackground() {
        return result = performLoad();
    }

    @Override
    protected void onStartLoading() {
        if (result != null) {
            deliverResult(result);
        }

        if (takeContentChanged() || result == null) {
            forceLoad();
        }
    }

    @Override
    protected void onReset() {
        super.onReset();

        result = null;
    }

    protected abstract T performLoad();
}
