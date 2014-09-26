package com.contentful.discovery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.contentful.discovery.R;
import com.contentful.discovery.api.ResourceList;
import com.contentful.discovery.ui.FieldViewHolder;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;
import com.contentful.java.model.CDAResource;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * Resources Adapter.
 */
public class ResourcesAdapter extends BaseAdapter {
    private final Context context;
    private ResourceList data;

    public ResourcesAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.resources.size();
    }

    @Override
    public CDAResource getItem(int position) {
        return data.resources.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.view_field, parent, false);

            convertView.setTag(vh = new FieldViewHolder(convertView));

            vh.tvValue.setVisibility(View.GONE);
            vh.ivArrow.setVisibility(View.VISIBLE);

            vh.tvTitle.setFont(false);
        } else {
            vh = (FieldViewHolder) convertView.getTag();
        }

        CDAResource resource = getItem(position);
        String name = null;

        if (resource instanceof CDAEntry) {
            String displayField = getEntryDisplayField((CDAEntry) resource);
            name = (String) ((CDAEntry) resource).getFields().get(displayField);
        }

        if (StringUtils.isBlank(name)) {
            name = (String) resource.getSys().get("id");
        }

        vh.tvTitle.setText(name);

        return convertView;
    }

    private String getEntryDisplayField(CDAEntry entry) {
        return Utils.getContentTypeForEntry(data.contentTypes, entry).getDisplayField();
    }

    public Map<String, CDAContentType> getContentTypesMap() {
        return data.contentTypes;
    }

    public void setResourceList(ResourceList resourceList) {
        this.data = resourceList;
    }
}
