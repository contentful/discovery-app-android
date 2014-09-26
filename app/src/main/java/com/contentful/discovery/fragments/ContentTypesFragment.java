package com.contentful.discovery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;

import com.contentful.discovery.R;
import com.contentful.discovery.activities.EntriesActivity;
import com.contentful.discovery.adapters.ContentTypesAdapter;
import com.contentful.discovery.api.ContentTypeWrapper;
import com.contentful.discovery.loaders.ContentTypesLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

import java.util.ArrayList;

import butterknife.OnItemClick;

/**
 * Content Types Fragment.
 */
public class ContentTypesFragment extends CFListFragment implements
        LoaderManager.LoaderCallbacks<ArrayList<ContentTypeWrapper>> {

    private ContentTypesAdapter adapter;

    public static ContentTypesFragment newInstance() {
        return new ContentTypesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new ContentTypesAdapter(getActivity());
        getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView.setAdapter(adapter);
    }

    @Override
    public Loader<ArrayList<ContentTypeWrapper>> onCreateLoader(int id, Bundle args) {
        return new ContentTypesLoader();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ContentTypeWrapper>> loader, ArrayList<ContentTypeWrapper> data) {
        boolean error = data == null;

        if (error || data.size() == 0) {
            showNoResults();

            if (error) {
                Utils.showGenericError(getActivity());
            }
        } else {
            adapter.setData(data);
            adapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ContentTypeWrapper>> loader) {

    }

    @OnItemClick(R.id.list)
    void onItemClick(int position) {
        ContentTypeWrapper contentTypeWrapper = adapter.getItem(position);

        if (contentTypeWrapper.getEntriesCount() > 0) {
            startActivity(new Intent(getActivity(), EntriesActivity.class)
                    .putExtra(IntentConsts.EXTRA_CONTENT_TYPE,
                            contentTypeWrapper.getContentType()));
        }
    }
}
