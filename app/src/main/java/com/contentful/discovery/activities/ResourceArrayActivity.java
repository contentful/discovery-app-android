package com.contentful.discovery.activities;

import android.os.Bundle;
import android.support.v4.content.Loader;

import com.contentful.discovery.api.ResourceList;
import com.contentful.discovery.loaders.ResourceArrayLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

import java.util.ArrayList;

/**
 * Resource Array Activity.
 * Displays a static collection of CDA resources inside a list.
 */
public class ResourceArrayActivity extends ResourceListActivity {
    @Override
    protected void initLoader() {
        getSupportLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
    }

    @Override
    protected void restartLoader() {
        getSupportLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Loader<ResourceList> onCreateLoader(int i, Bundle bundle) {
        return new ResourceArrayLoader((ArrayList<Object>) getIntent()
                .getSerializableExtra(IntentConsts.EXTRA_LIST));
    }
}
