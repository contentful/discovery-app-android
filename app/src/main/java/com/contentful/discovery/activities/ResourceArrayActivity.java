package com.contentful.discovery.activities;

import android.os.Bundle;
import android.support.v4.content.Loader;
import com.contentful.discovery.api.ResourceList;
import com.contentful.discovery.loaders.ResourceArrayLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import java.util.ArrayList;

public class ResourceArrayActivity extends ResourceListActivity {
  @Override protected void initLoader() {
    getSupportLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
  }

  @Override protected void restartLoader() {
    getSupportLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
  }

  @SuppressWarnings("unchecked")
  @Override
  public Loader<ResourceList> onCreateLoader(int i, Bundle bundle) {
    return new ResourceArrayLoader(
        (ArrayList<Object>) getIntent().getSerializableExtra(IntentConsts.EXTRA_LIST));
  }
}
