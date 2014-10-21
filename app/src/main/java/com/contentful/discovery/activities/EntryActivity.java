package com.contentful.discovery.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import com.contentful.discovery.adapters.EntryPagerAdapter;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;
import java.util.Map;

/**
 * Entry Activity.
 * Displays a single Entry in various forms using a tabbed layout.
 */
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
