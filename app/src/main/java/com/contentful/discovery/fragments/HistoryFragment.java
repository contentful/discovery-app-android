package com.contentful.discovery.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.contentful.discovery.R;
import com.contentful.discovery.adapters.HistoryAdapter;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.events.ChangeCredentialsEvent;
import com.contentful.discovery.events.ReloadHistoryEvent;
import com.contentful.discovery.loaders.CredentialsLoader;
import com.contentful.discovery.utils.CFBusProvider;
import com.contentful.discovery.utils.Utils;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnItemClick;

/**
 * History Fragment.
 * Displays a list of all previously used Credentials which resulted in successful authentications.
 */
public class HistoryFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<LinkedHashSet<Credentials>> {

    // Views
    @InjectView(R.id.list) ListView listView;

    private HistoryAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        adapter = new HistoryAdapter(getActivity());
        getLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
        CFBusProvider.getInstance().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_history, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inject views
        ButterKnife.inject(this, view);

        // Configure ListView
        listView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        ButterKnife.reset(this);

        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        CFBusProvider.getInstance().unregister(this);

        super.onDestroy();
    }

    @Override
    public Loader<LinkedHashSet<Credentials>> onCreateLoader(int i, Bundle bundle) {
        return new CredentialsLoader();
    }

    @Override
    public void onLoadFinished(Loader<LinkedHashSet<Credentials>> hashSetLoader,
                               LinkedHashSet<Credentials> credentials) {

        adapter.setData(new ArrayList<Credentials>(credentials));
        adapter.notifyDataSetInvalidated();
    }

    @Override
    public void onLoaderReset(Loader<LinkedHashSet<Credentials>> hashSetLoader) {

    }

    @OnItemClick(R.id.list)
    void onItemClicked(int position) {
        Credentials credentials = adapter.getItem(position);
        CFBusProvider.getInstance().post(new ChangeCredentialsEvent(credentials));
        getActivity().onBackPressed();
    }

    @Subscribe
    public void onReloadHistoryRequested(ReloadHistoryEvent e) {
        getLoaderManager().restartLoader(Utils.getLoaderId(this), null, this);
    }
}
