package com.contentful.discovery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;
import com.contentful.discovery.R;
import com.contentful.discovery.adapters.ResourcesAdapter;
import com.contentful.discovery.api.ResourceList;
import com.contentful.discovery.ui.AbsListContainer;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import java.io.Serializable;
import java.util.Map;

public abstract class ResourceListActivity extends CFListActivity
    implements LoaderManager.LoaderCallbacks<ResourceList> {

  protected ResourcesAdapter adapter;

  @Override protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);

    // Configure ListView
    listView.setAdapter(adapter = new ResourcesAdapter(this));

    // Initialize Loader
    initLoader();

    listContainerView.setListener(new AbsListContainer.Listener() {
      @Override public void retryLoad() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        restartLoader();
      }
    });
  }

  @Override public void onLoadFinished(Loader<ResourceList> resourceListLoader,
      ResourceList resourceList) {
    if (resourceList == null || resourceList.resources == null) {
      getListContainerView().showExtraView(R.id.network_error);
    } else if (resourceList.resources.size() == 0) {
      getListContainerView().showExtraView(R.id.no_results);
    } else {
      getListContainerView().hideExtraViews();
      adapter.setResourceList(resourceList);
      adapter.notifyDataSetInvalidated();
    }
  }

  @Override public void onLoaderReset(Loader<ResourceList> resourceListLoader) {
  }

  protected abstract void initLoader();

  protected abstract void restartLoader();

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    super.onItemClick(parent, view, position, id);

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
