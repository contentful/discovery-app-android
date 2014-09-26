package com.contentful.discovery.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.GridView;

import com.contentful.discovery.R;
import com.contentful.discovery.activities.AssetPreviewActivity;
import com.contentful.discovery.adapters.AssetsAdapter;
import com.contentful.discovery.loaders.AssetsLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAAsset;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * Assets Fragment.
 * Displays a collection of Assets inside a list.
 */
public class AssetsFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<ArrayList<CDAAsset>> {

    // Views
    @InjectView(R.id.grid) GridView gridView;
    @InjectView(R.id.empty) View emptyView;
    @InjectView(R.id.stub_no_results) ViewStub stubNoResults;

    private AssetsAdapter adapter;

    public static AssetsFragment newInstance() {
        return new AssetsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        adapter = new AssetsAdapter(getActivity());
        getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_assets, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inject views
        ButterKnife.inject(this, view);

        gridView.setAdapter(adapter);
        gridView.setEmptyView(emptyView);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);

        super.onDestroyView();
    }

    @Override
    public Loader<ArrayList<CDAAsset>> onCreateLoader(int id, Bundle args) {
        return new AssetsLoader();
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<CDAAsset>> loader, ArrayList<CDAAsset> data) {
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
    public void onLoaderReset(Loader<ArrayList<CDAAsset>> loader) {

    }

    @OnItemClick(R.id.grid)
    void onItemClick(int position, View v) {
        startActivity(new Intent(getActivity(), AssetPreviewActivity.class)
                .putExtra(IntentConsts.EXTRA_ASSET, adapter.getItem(position)));
    }

    private void showNoResults() {
        Utils.showNoResults(gridView, stubNoResults, emptyView);
    }
}
