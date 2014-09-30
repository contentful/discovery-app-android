package com.contentful.discovery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;

import com.contentful.discovery.R;
import com.contentful.discovery.loaders.EntryPreviewLoader;
import com.contentful.discovery.preview.EntryPreviewAdapter;
import com.contentful.discovery.ui.DisplayItem;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;

import java.util.List;

/**
 * Entry Preview Fragment.
 * Displays a single Entry in preview mode.
 */
public class EntryPreviewFragment extends CFListFragment implements
        LoaderManager.LoaderCallbacks<List<DisplayItem>> {

    private CDAEntry entry;
    private CDAContentType contentType;
    private EntryPreviewAdapter adapter;

    public static EntryPreviewFragment newInstance(CDAEntry entry, CDAContentType contentType) {
        EntryPreviewFragment entryPreviewFragment = new EntryPreviewFragment();

        Bundle b = new Bundle();
        b.putSerializable(IntentConsts.EXTRA_ENTRY, entry);
        b.putSerializable(IntentConsts.EXTRA_CONTENT_TYPE, contentType);
        entryPreviewFragment.setArguments(b);

        return entryPreviewFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        entry = (CDAEntry) getArguments().getSerializable(IntentConsts.EXTRA_ENTRY);

        contentType = (CDAContentType) getArguments()
                .getSerializable(IntentConsts.EXTRA_CONTENT_TYPE);

        getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);

        adapter = new EntryPreviewAdapter(getActivity());
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure ListView
        listView.setAdapter(adapter);
        listView.setRecyclerListener(new AbsListView.RecyclerListener() {
            @Override
            public void onMovedToScrapHeap(View view) {
                adapter.reset(view);
            }
        });
    }

    @Override
    public Loader<List<DisplayItem>> onCreateLoader(int id, Bundle args) {
        return new EntryPreviewLoader(entry, contentType);
    }

    @Override
    public void onLoadFinished(Loader<List<DisplayItem>> loader, List<DisplayItem> data) {
        if (data == null || data.size() == 0) {
            listContainerView.showExtraView(R.id.no_results);
        } else {
            getListContainerView().hideExtraViews();
            adapter.setData(data);
            adapter.notifyDataSetInvalidated();
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DisplayItem>> loader) {

    }
}
