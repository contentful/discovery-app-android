package com.contentful.discovery.adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.contentful.discovery.R;
import com.contentful.discovery.fragments.EntryPreviewFragment;
import com.contentful.discovery.preview.EntryListFragment;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import java.util.Map;

public class EntryPagerAdapter extends FragmentPagerAdapter {
  private final Context context;
  private final CDAEntry entry;
  private final CDAContentType contentType;
  private Map<String, CDAContentType> contentTypesMap;
  private static final int COUNT = 2;

  public EntryPagerAdapter(Context context, FragmentManager fm, CDAEntry entry,
      CDAContentType contentType, Map<String, CDAContentType> contentTypesMap) {

    super(fm);
    this.context = context;
    this.entry = entry;
    this.contentType = contentType;
    this.contentTypesMap = contentTypesMap;
  }

  @Override public int getCount() {
    return COUNT;
  }

  @Override public Fragment getItem(int position) {
    switch (position) {
      case 0:
        return EntryListFragment.newInstance(entry, contentTypesMap);

      case 1:
        return EntryPreviewFragment.newInstance(entry, contentType);

      default:
        throw new IllegalStateException("Invalid adapter count");
    }
  }

  @Override public CharSequence getPageTitle(int position) {
    switch (position) {
      case 0:
        return context.getString(R.string.tab_list);

      case 1:
        return context.getString(R.string.tab_preview);

      default:
        throw new IllegalStateException("Invalid adapter count");
    }
  }
}
