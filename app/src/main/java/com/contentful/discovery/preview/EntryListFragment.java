package com.contentful.discovery.preview;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.contentful.discovery.R;
import com.contentful.discovery.activities.AssetPreviewActivity;
import com.contentful.discovery.activities.EntryActivity;
import com.contentful.discovery.activities.MapActivity;
import com.contentful.discovery.activities.ResourceArrayActivity;
import com.contentful.discovery.activities.StringListActivity;
import com.contentful.discovery.activities.TextPreviewActivity;
import com.contentful.discovery.fragments.CFListFragment;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.lib.Constants;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;
import com.contentful.java.model.CDAResource;
import com.google.android.gms.maps.model.LatLng;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import butterknife.OnItemClick;

import static com.contentful.java.lib.Constants.CDAFieldType;

/**
 * Entry List Fragment.
 */
public class EntryListFragment extends CFListFragment {
    private CDAEntry entry;
    private CDAContentType contentType;
    private EntryListAdapter adapter;
    private Map<String, Map> contentTypesFields;
    private Map<String, CDAContentType> contentTypesMap;

    public static EntryListFragment newInstance(CDAEntry entry, Map<String, CDAContentType> contentTypesMap) {
        EntryListFragment fragment = new EntryListFragment();

        Bundle b = new Bundle();
        b.putSerializable(IntentConsts.EXTRA_ENTRY, entry);
        b.putSerializable(IntentConsts.EXTRA_CONTENT_TYPES_MAP, (Serializable) contentTypesMap);
        fragment.setArguments(b);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Extract arguments from Intent
        entry = (CDAEntry) getArguments().getSerializable(IntentConsts.EXTRA_ENTRY);

        contentTypesMap = (Map<String, CDAContentType>) getArguments()
                .getSerializable(IntentConsts.EXTRA_CONTENT_TYPES_MAP);

        contentType = Utils.getContentTypeForEntry(contentTypesMap, entry);

        // Content Type fields
        contentTypesFields = new HashMap<String, Map>();

        for (Map f : contentType.getFields()) {
            contentTypesFields.put((String) f.get("id"), f);
        }

        // Adapter
        adapter = new EntryListAdapter(getActivity(), entry, contentType);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Configure ListView
        listView.setAdapter(adapter);
    }

    @OnItemClick(R.id.list)
    void onItemClick(int position) {
        Map item = adapter.getItem(position);
        CDAFieldType fieldType = CDAFieldType.valueOf((String) item.get("type"));
        Object value = entry.getFields().get(item.get("id"));

        if (value != null) {
            if (CDAFieldType.Object.equals(fieldType) ||
                    CDAFieldType.Text.equals(fieldType) ||
                    CDAFieldType.Symbol.equals(fieldType)) {

                onTextItemClicked(item, value);
            } else if (CDAFieldType.Location.equals(fieldType)) {
                onLocationItemClicked(item, value);
            } else if (CDAFieldType.Link.equals(fieldType)) {
                onLinkItemClicked(item, value);
            } else if (CDAFieldType.Array.equals(fieldType)) {
                onArrayItemClicked(item, value);
            }
        }
    }

    /**
     * Click handler for a field of type {@code Array}.
     */
    private void onArrayItemClicked(Map item, Object value) {
        String id = (String) item.get("id");

        Map items = (Map) contentTypesFields.get(id).get("items");
        String arrayItemsType = (String) items.get("type");

        if ("Link".equalsIgnoreCase(arrayItemsType)) {
            startActivity(new Intent(getActivity(), ResourceArrayActivity.class)
                    .putExtra(IntentConsts.EXTRA_LIST, (Serializable) value));
        } else {
            startActivity(new Intent(getActivity(), StringListActivity.class)
                    .putExtra(IntentConsts.EXTRA_LIST, (Serializable) value));
        }
    }

    /**
     * Click handler for a field of type {@code Location}.
     */
    private void onLocationItemClicked(Map item, Object value) {
        Map<String, Object> map = (Map) value;

        startActivity(new Intent(getActivity(), MapActivity.class)
                .putExtra(IntentConsts.EXTRA_TITLE, (String) item.get("name"))
                .putExtra(IntentConsts.EXTRA_LOCATION,
                        new LatLng((Double) map.get("lat"), (Double) map.get("lon"))));
    }

    /**
     * Click handler for a field of type {@code Text}.
     */
    private void onTextItemClicked(Map item, Object value) {
        startActivity(new Intent(getActivity(), TextPreviewActivity.class)
                .putExtra(IntentConsts.EXTRA_TITLE, (String) item.get("name"))
                .putExtra(IntentConsts.EXTRA_TEXT, value.toString()));
    }

    /**
     * Click handler for a field of type {@code Link}.
     */
    private void onLinkItemClicked(Map item, Object value) {
        CDAResource resource = (CDAResource) value;

        Constants.CDAResourceType linkType = Constants.CDAResourceType.valueOf(
                (String) item.get("linkType"));

        if (Constants.CDAResourceType.Asset.equals(linkType)) {
            startActivity(new Intent(getActivity(), AssetPreviewActivity.class)
                    .putExtra(IntentConsts.EXTRA_TITLE, (String) item.get("name"))
                    .putExtra(IntentConsts.EXTRA_ASSET, resource));
        } else if (Constants.CDAResourceType.Entry.equals(linkType)) {
            CDAEntry linkedEntry = (CDAEntry) resource;

            startActivity(new Intent(getActivity(), EntryActivity.class)
                    .putExtra(IntentConsts.EXTRA_ENTRY, linkedEntry)
                    .putExtra(IntentConsts.EXTRA_CONTENT_TYPES_MAP, (Serializable) contentTypesMap));
        }
    }
}
