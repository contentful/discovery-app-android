package com.contentful.discovery.preview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.contentful.discovery.ui.DisplayItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EntryPreviewAdapter extends BaseAdapter {
  // View types
  public static final int VIEW_TYPE_GENERIC = 0;
  public static final int VIEW_TYPE_RTF = 1;
  public static final int VIEW_TYPE_ARRAY = 2;

  private final Context context;
  private List<DisplayItem> data = new ArrayList<>();

  // View Factories
  private static final GenericViewFactory VF_GENERIC = new GenericViewFactory();
  private static final RTFViewFactory VF_RTF = new RTFViewFactory();
  private static final ArrayViewFactory VF_ARRAY = new ArrayViewFactory();

  // View Factory mapping
  private HashMap<String, PreviewViewFactory> typeToViewFactoryMap =
      new HashMap<>();

  public EntryPreviewAdapter(Context context) {
    this.context = context;

    // GenericViewFactory
    registerViewFactory("Boolean", VF_GENERIC);
    registerViewFactory("Date", VF_GENERIC);
    registerViewFactory("Integer", VF_GENERIC);
    registerViewFactory("Number", VF_GENERIC);
    registerViewFactory("Object", VF_GENERIC);
    registerViewFactory("Symbol", VF_GENERIC);
    registerViewFactory("Location", VF_GENERIC);
    registerViewFactory("Link", VF_GENERIC);

    // ArrayViewFactory
    registerViewFactory("Array", VF_ARRAY);

    // RTFViewFactory
    registerViewFactory("Text", VF_RTF);
  }

  @Override public int getCount() {
    return data.size();
  }

  @Override public DisplayItem getItem(int position) {
    return data.get(position);
  }

  @Override public long getItemId(int position) {
    return 0;
  }

  @Override public int getViewTypeCount() {
    return 3;
  }

  @Override public int getItemViewType(int position) {
    return getFactory(getItem(position)).getItemViewType();
  }

  private PreviewViewFactory getFactory(DisplayItem item) {
    return typeToViewFactoryMap.get(item.fieldType);
  }

  @Override public View getView(int position, View convertView, ViewGroup parent) {
    DisplayItem displayItem = getItem(position);

    return getFactory(displayItem).getView(context, convertView, parent, displayItem,
        displayItem.fieldType).rootView;
  }

  public void setData(List<DisplayItem> data) {
    this.data = data;
  }

  private void registerViewFactory(String fieldType, PreviewViewFactory factory) {
    typeToViewFactoryMap.put(fieldType, factory);
  }

  @SuppressWarnings({ "SuspiciousMethodCalls", "unchecked" })
  public void reset(View v) {
    Object tag = v.getTag();

    if (tag instanceof AbsViewHolder) {
      AbsViewHolder vh = (AbsViewHolder) tag;
      typeToViewFactoryMap.get(vh.factoryKey).reset(vh);
    }
  }
}
