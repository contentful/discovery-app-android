package discovery.contentful.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import discovery.contentful.adapters.EntryPagerAdapter;
import discovery.contentful.utils.IntentConsts;
import discovery.contentful.utils.Utils;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import java.util.Map;

public class EntryActivity extends AbsTabsActivity {
  private CDAEntry entry;
  private CDAContentType contentType;
  private Map<String, CDAContentType> contentTypesMap;
  private EntryPagerAdapter adapter;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    viewPager.setCurrentItem(1);
  }

  @SuppressWarnings("unchecked")
  @Override
  protected void initOnCreate(Bundle savedInstanceState) {
    entry = (CDAEntry) getIntent().getSerializableExtra(IntentConsts.EXTRA_ENTRY);

    contentTypesMap = (Map<String, CDAContentType>) getIntent().getSerializableExtra(
        IntentConsts.EXTRA_CONTENT_TYPES_MAP);

    contentType = Utils.getContentTypeForEntry(contentTypesMap, entry);
  }

  @Override protected String getTitleForActivity() {
    return Utils.getTitleForEntry(entry, contentType);
  }

  @Override protected PagerAdapter getAdapter() {
    if (adapter == null) {
      adapter = new EntryPagerAdapter(this, getSupportFragmentManager(), entry, contentType,
          contentTypesMap);
    }

    return adapter;
  }
}
