package com.contentful.discovery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AdapterView;

import com.contentful.discovery.R;
import com.contentful.discovery.activities.EntriesActivity;
import com.contentful.discovery.adapters.ContentTypesAdapter;
import com.contentful.discovery.api.ContentTypeWrapper;
import com.contentful.discovery.loaders.ContentTypesLoader;
import com.contentful.discovery.ui.AbsListContainer;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

import java.util.ArrayList;

/**
 * Content Types Fragment.
 */
public class ContentTypesFragment extends CFListFragment implements
        LoaderManager.LoaderCallbacks<ArrayList<ContentTypeWrapper>>,
        AbsListContainer.Listener {

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
        listContainerView.setListener(this);
    }

    @Override
    public Loader<ArrayList<ContentTypeWrapper>> onCreateLoader(int id, Bundle args) {
        return new ContentTypesLoader();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ContentTypeWrapper>> loader, ArrayList<ContentTypeWrapper> data) {
        if (data == null) {
            getListContainerView().showExtraView(R.id.network_error);
        } else if (data.size() == 0) {
            getListContainerView().showExtraView(R.id.no_results);
        } else {
            getListContainerView().hideExtraViews();
            adapter.setData(data);
            adapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<ContentTypeWrapper>> loader) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        super.onItemClick(parent, view, position, id);

        ContentTypeWrapper contentTypeWrapper = adapter.getItem(position);

        if (contentTypeWrapper.getEntriesCount() > 0) {
            startActivity(new Intent(getActivity(), EntriesActivity.class)
                    .putExtra(IntentConsts.EXTRA_CONTENT_TYPE,
                            contentTypeWrapper.getContentType()));
        }
    }

    @Override
    public void retryLoad() {
        adapter.clear();
        adapter.notifyDataSetChanged();
        getLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
    }
}
