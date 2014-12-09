package com.contentful.discovery.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import com.contentful.discovery.adapters.HistoryAdapter;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.loaders.CredentialsLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import java.util.ArrayList;
import java.util.LinkedHashSet;

/**
 * History Activity.
 * Displays a list of all previously used Credentials which resulted in successful authentications.
 */
public class HistoryActivity extends CFListActivity
    implements LoaderManager.LoaderCallbacks<LinkedHashSet<Credentials>> {

  private HistoryAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Configure ListView
    listView.setAdapter(adapter = new HistoryAdapter(this));

    // Init Loader
    getSupportLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
  }

  @Override public boolean onOptionsItemSelected(MenuItem item) {
    boolean ret = super.onOptionsItemSelected(item);

    switch (item.getItemId()) {
      case android.R.id.home:
        finish();
        return true;

      default:
    }

    return ret;
  }

  @Override public Loader<LinkedHashSet<Credentials>> onCreateLoader(int i, Bundle bundle) {
    return new CredentialsLoader();
  }

  @Override public void onLoadFinished(Loader<LinkedHashSet<Credentials>> loader,
      LinkedHashSet<Credentials> credentials) {

    adapter.setData(new ArrayList<>(credentials));
    adapter.notifyDataSetInvalidated();
  }

  @Override public void onLoaderReset(Loader<LinkedHashSet<Credentials>> loader) {
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    super.onItemClick(parent, view, position, id);

    Credentials credentials = adapter.getItem(position);

    setResult(Activity.RESULT_OK,
        new Intent().putExtra(IntentConsts.EXTRA_CREDENTIALS, credentials));

    finish();
  }
}
