package com.contentful.discovery.activities;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import com.contentful.discovery.ui.AbsListContainer;

/**
 * ListActivity.
 */
@SuppressLint("Registered")
public class CFListActivity extends CFFragmentActivity implements AdapterView.OnItemClickListener {
  // Views
  AbsListContainer<ListView> listContainerView;
  ListView listView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    listView = new ListView(this);

    listView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT));

    setContentView(listContainerView = new AbsListContainer<ListView>(this) {
      @Override protected ListView inflateList() {
        return listView;
      }
    });

    listView.setOnItemClickListener(this);
  }

  public AbsListContainer<ListView> getListContainerView() {
    return listContainerView;
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
  }
}