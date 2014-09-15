package com.contentful.discovery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.contentful.discovery.R;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.model.CDAAsset;

import java.util.ArrayList;

/**
 * Assets Adapter.
 */
public class AssetsAdapter extends BaseAdapter {
    private final Context context;
    private ArrayList<CDAAsset> data = new ArrayList<CDAAsset>();

    public AssetsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public CDAAsset getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public void setData(ArrayList<CDAAsset> data) {
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.view_asset, parent, false);
            convertView.setTag(vh = new ViewHolder(convertView));
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        Utils.loadThumbnailForAsset(context, getItem(position), vh.ivThumbnail);

        return convertView;
    }

    /**
     * View Holder
     */
    private class ViewHolder {
        final ImageView ivThumbnail;

        ViewHolder(View v) {
            this.ivThumbnail = (ImageView) v.findViewById(R.id.iv_thumbnail);
        }
    }
}
