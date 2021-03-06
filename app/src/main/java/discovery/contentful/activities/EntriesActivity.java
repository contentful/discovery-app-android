package discovery.contentful.activities;

import android.os.Bundle;
import android.support.v4.content.Loader;
import discovery.contentful.api.ResourceList;
import discovery.contentful.loaders.EntriesByContentTypeLoader;
import discovery.contentful.utils.IntentConsts;
import discovery.contentful.utils.Utils;
import com.contentful.java.cda.CDAContentType;

public class EntriesActivity extends ResourceListActivity {
  private CDAContentType contentType;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    contentType =
        (CDAContentType) getIntent().getSerializableExtra(IntentConsts.EXTRA_CONTENT_TYPE);

    setTitle(contentType.name());
  }

  @Override protected void initLoader() {
    getSupportLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
  }

  @Override protected void restartLoader() {
    getSupportLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
  }

  @Override public Loader<ResourceList> onCreateLoader(int i, Bundle bundle) {
    return new EntriesByContentTypeLoader(contentType);
  }
}