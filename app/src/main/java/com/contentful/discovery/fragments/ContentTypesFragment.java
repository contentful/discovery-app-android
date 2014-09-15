package com.contentful.discovery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.contentful.discovery.ContentTypeWrapper;
import com.contentful.discovery.R;
import com.contentful.discovery.activities.EntriesActivity;
import com.contentful.discovery.adapters.ContentTypesAdapter;
import com.contentful.discovery.loaders.ContentTypesLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Created by tom on 03/09/14.
 */
public class ContentTypesFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<ContentTypeWrapper>> {

    // Views
    @InjectView(R.id.list) ListView listView;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_content_types, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inject views
        ButterKnife.inject(this, view);

        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);

        super.onDestroyView();
    }

    @Override
    public Loader<ArrayList<ContentTypeWrapper>> onCreateLoader(int id, Bundle args) {
        return new ContentTypesLoader();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<ContentTypeWrapper>> loader, ArrayList<ContentTypeWrapper> data) {
        adapter.setData(data);
        adapter.notifyDataSetInvalidated();
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
