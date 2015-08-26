package com.contentful.discovery.adapters;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.ui.FieldViewHolder;
import com.contentful.discovery.utils.ViewHelper;
import com.contentful.java.cda.CDAAsset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.io.FileUtils;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

public class AssetInfoAdapter extends BaseAdapter {
  private final Context context;

  private final ArrayList<Pair<String, String>> fields;

  private final static HashSet<String> CLICKABLE_TITLES =
      new HashSet<>(Arrays.asList(new String[] {
          CFApp.getInstance().getString(R.string.asset_info_title),
          CFApp.getInstance().getString(R.string.asset_info_description)
      }));

  public AssetInfoAdapter(Context context, CDAAsset asset) {
    this.context = context;

    fields = new ArrayList<>();

    // Title
    fields.add(new Pair<>(context.getString(R.string.asset_info_title),
        (String) asset.getField("title")));

    // Description
    fields.add(new Pair<>(context.getString(R.string.asset_info_description),
        (String) asset.getField("description")));

    // Creation Date
    DateTime createdAt = DateTime.parse((String) asset.getAttribute("createdAt"));

    fields.add(new Pair<>(context.getString(R.string.asset_info_created_at),
        createdAt.toString(DateTimeFormat.forStyle("MM"))));

    // MIME Type
    fields.add(new Pair<>(context.getString(R.string.asset_info_mime_type), asset.mimeType()));

    Map details = asset.fileField("details");
    Double sizeInBytes = (Double) details.get("size");
    String displaySize = FileUtils.byteCountToDisplaySize(sizeInBytes.longValue());

    fields.add(new Pair<>(context.getString(R.string.asset_info_size), displaySize));
  }

  @Override public int getCount() {
    return fields.size();
  }

  @Override public Pair<String, String> getItem(int position) {
    return fields.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    FieldViewHolder vh;

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.view_field, parent, false);

      convertView.setTag(vh = new FieldViewHolder(convertView));

      vh.tvValue.setVisibility(View.VISIBLE);
    } else {
      vh = new FieldViewHolder(convertView);
    }

    Pair<String, String> item = getItem(position); // Title, Value

    vh.tvTitle.setText(item.first);
    vh.tvValue.setText(item.second);

    boolean clickable = CLICKABLE_TITLES.contains(item.first);
    convertView.setTag(R.id.tag_clickable, clickable);
    ViewHelper.setVisibility(vh.ivArrow, clickable);

    return convertView;
  }
}
