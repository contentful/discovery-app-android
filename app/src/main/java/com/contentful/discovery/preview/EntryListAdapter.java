package com.contentful.discovery.preview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.contentful.discovery.R;
import com.contentful.discovery.ui.FieldViewHolder;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.contentful.java.lib.Constants.CDAFieldType;

/**
 * Entry List Adapter.
 */
public class EntryListAdapter extends BaseAdapter {
    private final Context context;
    private final CDAEntry entry;
    private final List<Map> contentTypeFields;

    public EntryListAdapter(Context context, CDAEntry entry, CDAContentType contentType) {
        this.context = context;
        this.entry = entry;
        this.contentTypeFields = contentType.getFields();
    }

    @Override
    public int getCount() {
        return contentTypeFields.size();
    }

    @Override
    public Map getItem(int position) {
        return contentTypeFields.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        FieldViewHolder vh;

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.view_field, null);
            convertView.setTag(vh = new FieldViewHolder(convertView));
        } else {
            vh = (FieldViewHolder) convertView.getTag();
        }

        Map item = getItem(position);
        Object value = entry.getFields().get(item.get("id"));
        CDAFieldType fieldType = CDAFieldType.valueOf((String) item.get("type"));

        // Title
        vh.tvTitle.setText((CharSequence) item.get("name"));

        // Value
        if (value != null && isDisplayableFieldType(fieldType)) {
            vh.tvValue.setText(normalizeDisplayValue(fieldType, value));
            vh.tvValue.setVisibility(View.VISIBLE);
        } else {
            vh.tvValue.setVisibility(View.GONE);
        }

        // Arrow
        vh.ivArrow.setVisibility(value != null &&
                isClickableFieldType(fieldType) ?
                View.VISIBLE :
                View.GONE);

        return convertView;
    }

    private boolean isDisplayableFieldType(CDAFieldType fieldType) {
        return CDAFieldType.Boolean.equals(fieldType) ||
                CDAFieldType.Date.equals(fieldType) ||
                CDAFieldType.Integer.equals(fieldType) ||
                CDAFieldType.Number.equals(fieldType) ||
                CDAFieldType.Symbol.equals(fieldType) ||
                CDAFieldType.Text.equals(fieldType) ||
                CDAFieldType.Location.equals(fieldType);
    }

    private static boolean isClickableFieldType(CDAFieldType fieldType) {
        return CDAFieldType.Array.equals(fieldType) ||
                CDAFieldType.Link.equals(fieldType) ||
                CDAFieldType.Location.equals(fieldType) ||
                CDAFieldType.Object.equals(fieldType) ||
                CDAFieldType.Symbol.equals(fieldType) ||
                CDAFieldType.Text.equals(fieldType);
    }

    private static String normalizeDisplayValue(CDAFieldType fieldType, Object object) {
        if (CDAFieldType.Integer.equals(fieldType)) {
            return Integer.toString(((Double) object).intValue());
        } else if (CDAFieldType.Location.equals(fieldType)) {
            Map map = (Map) object;

            Double lat = (Double) map.get("lat");
            Double lon = (Double) map.get("lon");

            return String.format(Locale.getDefault(), "(%.2f, %.2f)", lat, lon);
        }

        return object.toString();
    }
}
