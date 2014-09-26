package com.contentful.discovery.activities;

import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.ListView;

import com.contentful.discovery.R;
import com.contentful.discovery.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List Activity.
 */
public class CFListActivity extends CFFragmentActivity {
    // Views
    protected @InjectView(R.id.list) ListView listView;
    @InjectView(R.id.empty) View emptyView;
    @InjectView(R.id.stub_no_results) ViewStub stubNoResults;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_list);

        // Inject views
        ButterKnife.inject(this);

        // Configure ListView
        listView.setEmptyView(emptyView);
    }

    protected void showNoResults() {
        Utils.showNoResults(listView, stubNoResults, emptyView);
    }
}