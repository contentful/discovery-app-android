package com.contentful.discovery.preview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.contentful.discovery.R;
import com.contentful.discovery.ui.FieldViewHolder;
import com.contentful.discovery.utils.ViewHelper;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAField;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EntryListAdapter extends BaseAdapter {
  private final Context context;
  private final CDAEntry entry;
  private final List<CDAField> contentTypeFields;

  public EntryListAdapter(Context context, CDAEntry entry, CDAContentType contentType) {
    this.context = context;
    this.entry = entry;
    this.contentTypeFields = contentType.fields();
  }

  @Override public int getCount() {
    return contentTypeFields.size();
  }

  @Override public CDAField getItem(int position) {
    return contentTypeFields.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    FieldViewHolder vh;

    if (convertView == null) {
      convertView = View.inflate(context, R.layout.view_field, null);
      convertView.setTag(vh = new FieldViewHolder(convertView));
    } else {
      vh = (FieldViewHolder) convertView.getTag();
    }

    CDAField item = getItem(position);
    Object value = entry.getField(item.id());
    String fieldType = item.type();

    // Title
    vh.tvTitle.setText(item.name());

    // Value
    if (value != null && isDisplayableFieldType(fieldType)) {
      vh.tvValue.setText(normalizeDisplayValue(fieldType, value));
      vh.tvValue.setVisibility(View.VISIBLE);
    } else {
      vh.tvValue.setVisibility(View.GONE);
    }

    // Arrow
    ViewHelper.setVisibility(vh.ivArrow, value != null && isClickableFieldType(fieldType));

    return convertView;
  }

  private boolean isDisplayableFieldType(String fieldType) {
    return "Boolean".equals(fieldType)
        || "Date".equals(fieldType)
        || "Integer".equals(fieldType)
        || "Number".equals(fieldType)
        || "Symbol".equals(fieldType)
        || "Text".equals(fieldType)
        || "Location".equals(fieldType);
  }

  private static boolean isClickableFieldType(String fieldType) {
    return "Array".equals(fieldType)
        || "Link".equals(fieldType)
        || "Location".equals(fieldType)
        || "Object".equals(fieldType)
        || "Symbol".equals(fieldType)
        || "Text".equals(fieldType);
  }

  private static String normalizeDisplayValue(String fieldType, Object object) {
    if ("Integer".equals(fieldType)) {
      return Integer.toString(((Double) object).intValue());
    } else if ("Location".equals(fieldType)) {
      Map map = (Map) object;

      Double lat = (Double) map.get("lat");
      Double lon = (Double) map.get("lon");

      return String.format(Locale.getDefault(), "(%.2f, %.2f)", lat, lon);
    }

    return object.toString();
  }
}
