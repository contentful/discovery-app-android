package com.contentful.discovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

import com.contentful.discovery.R;
import com.contentful.discovery.adapters.ResourcesAdapter;
import com.contentful.discovery.api.ResourceList;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.java.model.CDAAsset;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;
import com.contentful.java.model.CDAResource;

import java.io.Serializable;
import java.util.Map;

import butterknife.OnItemClick;

/**
 * Abstract {@code Resource} List {@code Activity}.
 * Displays a collection of CDA {@code Resources} in a list.
 */
public abstract class ResourceListActivity extends CFListActivity implements
        LoaderManager.LoaderCallbacks<ResourceList> {

    protected ResourcesAdapter adapter;

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        // Configure ListView
        listView.setAdapter(adapter = new ResourcesAdapter(this));

        // Initialize Loader
        initLoader();
    }

    @Override
    public void onLoadFinished(Loader<ResourceList> resourceListLoader, ResourceList resourceList) {
        adapter.setResourceList(resourceList);
        adapter.notifyDataSetInvalidated();
    }

    @Override
    public void onLoaderReset(Loader<ResourceList> resourceListLoader) {

    }

    protected abstract void initLoader();

    @OnItemClick(R.id.list)
    void onItemClick(int position) {
        CDAResource res = adapter.getItem(position);

        if (res instanceof CDAAsset) {
            onAssetClicked((CDAAsset) res);
        } else if (res instanceof CDAEntry) {
            onEntryClicked((CDAEntry) res);
        }
    }

    /**
     * Callback method to be called when an {@code Entry} is clicked.
     *
     * @param entry {@code CDAEntry} instance.
     */
    private void onEntryClicked(CDAEntry entry) {
        Map<String, CDAContentType> contentTypesMap = adapter.getContentTypesMap();

        startActivity(new Intent(this, EntryActivity.class)
                .putExtra(IntentConsts.EXTRA_ENTRY, entry)
                .putExtra(IntentConsts.EXTRA_CONTENT_TYPES_MAP, (Serializable) contentTypesMap));
    }

    /**
     * Callback method to be called when an {@code Asset} is clicked.
     *
     * @param asset {@code CDAAsset} instance.
     */
    private void onAssetClicked(CDAAsset asset) {
        startActivity(new Intent(this, AssetPreviewActivity.class)
                .putExtra(IntentConsts.EXTRA_ASSET, asset));
    }
}
