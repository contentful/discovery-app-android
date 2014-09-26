package com.contentful.discovery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ListView;

import com.contentful.discovery.R;
import com.contentful.discovery.utils.Utils;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * List Fragment.
 */
public class CFListFragment extends Fragment {
    // Views
    protected @InjectView(R.id.list) ListView listView;
    @InjectView(R.id.stub_no_results) ViewStub stubNoResults;
    @InjectView(R.id.empty) View emptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inject views
        ButterKnife.inject(this, view);

        // Configure ListView
        listView.setEmptyView(emptyView);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);
        super.onDestroyView();
    }

    protected void showNoResults() {
        Utils.showNoResults(listView, stubNoResults, emptyView);
    }
}
