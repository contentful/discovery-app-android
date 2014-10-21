package com.contentful.discovery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.contentful.discovery.ui.AbsListContainer;

/**
 * ListFragment.
 */
public class CFListFragment extends Fragment implements AdapterView.OnItemClickListener {
  protected ListView listView;
  AbsListContainer<ListView> listContainerView;

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);

    listView = new ListView(getActivity());

    listView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));

    listView.setOnItemClickListener(this);

    listContainerView = new AbsListContainer<ListView>(getActivity()) {
      @Override protected ListView inflateList() {
        return listView;
      }
    };

    return listContainerView;
  }

  public AbsListContainer<ListView> getListContainerView() {
    return listContainerView;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  }
}
