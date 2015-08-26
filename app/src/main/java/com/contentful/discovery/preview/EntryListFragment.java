package com.contentful.discovery.preview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.AdapterView;
import com.contentful.discovery.activities.AssetPreviewActivity;
import com.contentful.discovery.activities.EntryActivity;
import com.contentful.discovery.activities.MapActivity;
import com.contentful.discovery.activities.ResourceArrayActivity;
import com.contentful.discovery.activities.StringListActivity;
import com.contentful.discovery.activities.TextPreviewActivity;
import com.contentful.discovery.fragments.CFListFragment;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAField;
import com.contentful.java.cda.CDAResource;
import com.google.android.gms.maps.model.LatLng;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class EntryListFragment extends CFListFragment {
  private CDAEntry entry;
  private CDAContentType contentType;
  private EntryListAdapter adapter;
  private Map<String, CDAField> contentTypesFields;
  private Map<String, CDAContentType> contentTypesMap;

  public static EntryListFragment newInstance(CDAEntry entry,
      Map<String, CDAContentType> contentTypesMap) {
    EntryListFragment fragment = new EntryListFragment();
    Bundle b = new Bundle();
    b.putSerializable(IntentConsts.EXTRA_ENTRY, entry);
    b.putSerializable(IntentConsts.EXTRA_CONTENT_TYPES_MAP, (Serializable) contentTypesMap);
    fragment.setArguments(b);
    return fragment;
  }

  @SuppressWarnings("unchecked")
  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Extract arguments from Intent
    entry = (CDAEntry) getArguments().getSerializable(IntentConsts.EXTRA_ENTRY);

    contentTypesMap = (Map<String, CDAContentType>) getArguments().getSerializable(
        IntentConsts.EXTRA_CONTENT_TYPES_MAP);

    contentType = Utils.getContentTypeForEntry(contentTypesMap, entry);

    // Content Type fields
    contentTypesFields = new HashMap<>();

    for (CDAField f : contentType.fields()) {
      contentTypesFields.put(f.id(), f);
    }

    // Adapter
    adapter = new EntryListAdapter(getActivity(), entry, contentType);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    listView.setAdapter(adapter);
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    super.onItemClick(parent, view, position, id);

    CDAField item = adapter.getItem(position);
    String fieldType = item.type();
    Object value = entry.getField(item.id());

    if (value != null) {
      if ("Object".equals(fieldType)
          || "Text".equals(fieldType)
          || "Symbol".equals(fieldType)) {
        onTextItemClicked(item, value);
      } else if ("Location".equals(fieldType)) {
        onLocationItemClicked(item, value);
      } else if ("Link".equals(fieldType)) {
        onLinkItemClicked(item, value);
      } else if ("Array".equals(fieldType)) {
        onArrayItemClicked(item, value);
      }
    }
  }

  private void onArrayItemClicked(CDAField item, Object value) {
    String id = item.id();

    Map<String, Object> items = contentTypesFields.get(id).items();
    String arrayItemsType = (String) items.get("type");

    if ("Link".equalsIgnoreCase(arrayItemsType)) {
      startActivity(
          new Intent(getActivity(), ResourceArrayActivity.class)
              .putExtra(IntentConsts.EXTRA_LIST, (Serializable) value));
    } else {
      startActivity(
          new Intent(getActivity(), StringListActivity.class)
              .putExtra(IntentConsts.EXTRA_LIST, (Serializable) value));
    }
  }

  /**
   * Click handler for a field of type {@code Location}.
   */
  @SuppressWarnings("unchecked")
  private void onLocationItemClicked(CDAField item, Object value) {
    Map<String, Object> map = (Map) value;

    startActivity(new Intent(getActivity(), MapActivity.class)
        .putExtra(IntentConsts.EXTRA_TITLE, item.name())
        .putExtra(IntentConsts.EXTRA_LOCATION,
            new LatLng((Double) map.get("lat"), (Double) map.get("lon"))));
  }

  private void onTextItemClicked(CDAField item, Object value) {
    startActivity(new Intent(getActivity(), TextPreviewActivity.class)
        .putExtra(IntentConsts.EXTRA_TITLE, item.name())
        .putExtra(IntentConsts.EXTRA_TEXT, value.toString()));
  }

  /**
   * Click handler for a field of type {@code Link}.
   */
  private void onLinkItemClicked(CDAField item, Object value) {
    CDAResource resource = (CDAResource) value;

    String linkType = item.linkType();

    if ("Asset".equals(linkType)) {
      startActivity(new Intent(getActivity(), AssetPreviewActivity.class)
          .putExtra(IntentConsts.EXTRA_TITLE, item.name())
          .putExtra(IntentConsts.EXTRA_ASSET, resource));
    } else if ("Entry".equals(linkType)) {
      CDAEntry linkedEntry = (CDAEntry) resource;

      startActivity(new Intent(getActivity(), EntryActivity.class)
          .putExtra(IntentConsts.EXTRA_ENTRY, linkedEntry)
          .putExtra(IntentConsts.EXTRA_CONTENT_TYPES_MAP, (Serializable) contentTypesMap));
    }
  }
}
