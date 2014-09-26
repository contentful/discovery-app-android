package com.contentful.discovery.preview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.contentful.discovery.ui.DisplayItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.contentful.java.lib.Constants.CDAFieldType;

/**
 * Entry Preview Adapter.
 */
public class EntryPreviewAdapter extends BaseAdapter {
    // View types
    public static final int VIEW_TYPE_GENERIC = 0;
    public static final int VIEW_TYPE_RTF = 1;
    public static final int VIEW_TYPE_ARRAY = 2;

    private final Context context;
    private List<DisplayItem> data = new ArrayList<DisplayItem>();

    // View Factories
    private static final GenericViewFactory sGenericViewFactory = new GenericViewFactory();
    private static final RTFViewFactory sRTFViewFactory = new RTFViewFactory();
    private static final ArrayViewFactory sArrayViewFactory = new ArrayViewFactory();

    // View Factory mapping
    private HashMap<CDAFieldType, PreviewViewFactory> typeToViewFactoryMap =
            new HashMap<CDAFieldType, PreviewViewFactory>();

    public EntryPreviewAdapter(Context context) {
        this.context = context;

        // GenericViewFactory
        registerViewFactory(CDAFieldType.Boolean, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Date, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Integer, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Number, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Object, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Symbol, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Location, sGenericViewFactory);
        registerViewFactory(CDAFieldType.Link, sGenericViewFactory);

        // ArrayViewFactory
        registerViewFactory(CDAFieldType.Array, sArrayViewFactory);

        // RTFViewFactory
        registerViewFactory(CDAFieldType.Text, sRTFViewFactory);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public DisplayItem getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 3;
    }

    @Override
    public int getItemViewType(int position) {
        return getFactory(getItem(position)).getItemViewType();
    }

    private PreviewViewFactory getFactory(DisplayItem item) {
        return typeToViewFactoryMap.get(item.fieldType);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DisplayItem displayItem = getItem(position);

        return getFactory(displayItem).getView(
                context, convertView, parent, displayItem, displayItem.fieldType).rootView;
    }

    public void setData(List<DisplayItem> data) {
        this.data = data;
    }

    private void registerViewFactory(CDAFieldType fieldType, PreviewViewFactory factory) {
        typeToViewFactoryMap.put(fieldType, factory);
    }

    public void reset(View v) {
        Object tag = v.getTag();

        if (tag instanceof AbsViewHolder) {
            AbsViewHolder vh = (AbsViewHolder) tag;
            typeToViewFactoryMap.get(vh.factoryKey).reset(vh);
        }
    }
}
