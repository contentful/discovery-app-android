package discovery.contentful.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.View;
import android.widget.AbsListView;
import discovery.contentful.R;
import discovery.contentful.loaders.EntryPreviewLoader;
import discovery.contentful.preview.EntryPreviewAdapter;
import discovery.contentful.ui.DisplayItem;
import discovery.contentful.utils.IntentConsts;
import discovery.contentful.utils.Utils;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import java.util.List;

public class EntryPreviewFragment extends CFListFragment
    implements LoaderManager.LoaderCallbacks<List<DisplayItem>> {

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

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    entry = (CDAEntry) getArguments().getSerializable(IntentConsts.EXTRA_ENTRY);
    contentType = (CDAContentType) getArguments().getSerializable(IntentConsts.EXTRA_CONTENT_TYPE);
    adapter = new EntryPreviewAdapter(getActivity());
    getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    // Configure ListView
    listView.setAdapter(adapter);
    listView.setRecyclerListener(new AbsListView.RecyclerListener() {
      @Override public void onMovedToScrapHeap(View view) {
        adapter.reset(view);
      }
    });
  }

  @Override public Loader<List<DisplayItem>> onCreateLoader(int id, Bundle args) {
    return new EntryPreviewLoader(entry, contentType);
  }

  @Override public void onLoadFinished(Loader<List<DisplayItem>> loader, List<DisplayItem> data) {
    if (data == null || data.size() == 0) {
      listContainerView.showExtraView(R.id.no_results);
    } else {
      getListContainerView().hideExtraViews();
      adapter.setData(data);
      adapter.notifyDataSetInvalidated();
    }
  }

  @Override public void onLoaderReset(Loader<List<DisplayItem>> loader) {
  }
}
