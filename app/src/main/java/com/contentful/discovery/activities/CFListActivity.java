package com.contentful.discovery.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.contentful.discovery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List Activity.
 */
public class CFListActivity extends CFFragmentActivity {
    // Views
    protected @InjectView(R.id.list) ListView listView;
    @InjectView(R.id.empty) View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        // Inject views
        ButterKnife.inject(this);

        // Configure ListView
        listView.setEmptyView(emptyView);
    }
}
