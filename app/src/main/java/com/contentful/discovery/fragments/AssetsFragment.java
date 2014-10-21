package com.contentful.discovery.fragments;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import com.contentful.discovery.R;
import com.contentful.discovery.activities.AssetPreviewActivity;
import com.contentful.discovery.adapters.AssetsAdapter;
import com.contentful.discovery.loaders.AssetsLoader;
import com.contentful.discovery.ui.AbsListContainer;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAAsset;
import java.util.ArrayList;

/**
 * Assets Fragment.
 * Displays a collection of Assets inside a list.
 */
public class AssetsFragment extends Fragment
    implements LoaderManager.LoaderCallbacks<ArrayList<CDAAsset>>, AdapterView.OnItemClickListener,
    AbsListContainer.Listener {

  AbsListContainer<GridView> listContainerView;
  GridView gridView;

  private AssetsAdapter adapter;

  public static AssetsFragment newInstance() {
    return new AssetsFragment();
  }

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new AssetsAdapter(getActivity());
    getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    initGridView();
    gridView.setOnItemClickListener(this);

    listContainerView = new AbsListContainer<GridView>(getActivity()) {
      @Override protected GridView inflateList() {
        return gridView;
      }
    };

    return listContainerView;
  }

  @Override public void onViewCreated(View view, Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    gridView.setAdapter(adapter);
    listContainerView.setListener(this);
  }

  @Override public Loader<ArrayList<CDAAsset>> onCreateLoader(int id, Bundle args) {
    return new AssetsLoader();
  }

  @Override public void onLoadFinished(Loader<ArrayList<CDAAsset>> loader,
      ArrayList<CDAAsset> data) {
    if (data == null) {
      listContainerView.showExtraView(R.id.network_error);
    } else if (data.size() == 0) {
      listContainerView.showExtraView(R.id.no_results);
    } else {
      listContainerView.hideExtraViews();
      adapter.setData(data);
      adapter.notifyDataSetInvalidated();
    }
  }

  @Override public void onLoaderReset(Loader<ArrayList<CDAAsset>> loader) {
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    startActivity(
        new Intent(getActivity(), AssetPreviewActivity.class)
            .putExtra(IntentConsts.EXTRA_ASSET, adapter.getItem(position)));
  }

  private void initGridView() {

    gridView = new GridView(getActivity());

    gridView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));

    gridView.setColumnWidth(getResources().getDimensionPixelSize(R.dimen.assets_grid_column_width));
    gridView.setGravity(Gravity.CENTER);
    gridView.setSelector(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
    gridView.setNumColumns(GridView.AUTO_FIT);
    gridView.setStretchMode(GridView.STRETCH_SPACING_UNIFORM);
    int padding = getResources().getDimensionPixelSize(R.dimen.assets_grid_vertical_padding);
    gridView.setPadding(0, padding, 0, padding);
    gridView.setVerticalSpacing(padding);
  }

  @Override public void retryLoad() {
    adapter.clear();
    adapter.notifyDataSetChanged();
    getLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
  }
}
