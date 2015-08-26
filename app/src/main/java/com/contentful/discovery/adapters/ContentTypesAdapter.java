package com.contentful.discovery.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.contentful.discovery.R;
import com.contentful.discovery.api.ContentTypeWrapper;
import java.util.ArrayList;

public class ContentTypesAdapter extends BaseAdapter {
  private final Context context;

  private ArrayList<ContentTypeWrapper> data = new ArrayList<>();

  public ContentTypesAdapter(Context context) {
    this.context = context;
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public ContentTypeWrapper getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    ViewHolder vh;

    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.view_content_type, parent, false);

      convertView.setTag(vh = new ViewHolder(convertView));
    } else {
      vh = (ViewHolder) convertView.getTag();
    }

    ContentTypeWrapper contentTypeWrapper = getItem(position);
    convertView.setTag(R.id.tag_content_type, contentTypeWrapper);

    vh.tvName.setText(contentTypeWrapper.getContentType().name());
    vh.tvDescription.setText(contentTypeWrapper.getContentType().description());
    vh.tvEntriesCount.setText(
        context.getString(R.string.num_of_entries, contentTypeWrapper.getEntriesCount()));

    return convertView;
  }

  public void setData(ArrayList<ContentTypeWrapper> data) {
    this.data = data;
  }

  public void clear() {
    data.clear();
  }

  /**
   * View Holder
   */
  class ViewHolder {
    @Bind(R.id.tv_name) TextView tvName;
    @Bind(R.id.tv_description) TextView tvDescription;
    @Bind(R.id.tv_entries_count) TextView tvEntriesCount;

    ViewHolder(View v) {
      ButterKnife.bind(this, v);
    }
  }
}
