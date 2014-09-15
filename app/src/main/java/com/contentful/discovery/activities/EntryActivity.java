package com.contentful.discovery.activities;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;

import com.contentful.discovery.adapters.EntryPagerAdapter;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;

/**
 * Entry Activity.
 * Displays a single Entry in various forms using a tabbed layout.
 */
public class EntryActivity extends AbsTabsActivity {
    private CDAEntry entry;
    private CDAContentType contentType;
    private EntryPagerAdapter adapter;

    @Override
    protected void initOnCreate(Bundle savedInstanceState) {
        entry = (CDAEntry) getIntent().getSerializableExtra(IntentConsts.EXTRA_ENTRY);

        contentType = (CDAContentType) getIntent()
                .getSerializableExtra(IntentConsts.EXTRA_CONTENT_TYPE);
    }

    @Override
    protected String getTitleForActivity() {
        return (String) entry.getFields().get(contentType.getDisplayField());
    }

    @Override
    protected PagerAdapter getAdapter() {
        if (adapter == null) {
            adapter = new EntryPagerAdapter(this, getSupportFragmentManager(), entry, contentType);
        }

        return adapter;
    }
}
