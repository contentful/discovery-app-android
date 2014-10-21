package com.contentful.discovery.preview;

import android.view.View;
import android.widget.TextView;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.ui.DisplayItem;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.lib.Constants;
import com.contentful.java.model.CDAAsset;
import com.squareup.picasso.Picasso;

/**
 * Generic View Factory.
 */
public class GenericViewFactory extends PreviewViewFactory<GenericViewHolder> {
  @Override protected int getLayoutResId() {
    return R.layout.view_preview_item;
  }

  @Override protected GenericViewHolder createViewHolder(Object factoryKey, View v) {
    return new GenericViewHolder(factoryKey, v);
  }

  @Override protected void setViewData(GenericViewHolder viewHolder, DisplayItem displayItem) {
    if (Constants.CDAFieldType.Link.equals(displayItem.fieldType)) {
      setLinkData(viewHolder, displayItem);
    } else if (Constants.CDAFieldType.Location.equals(displayItem.fieldType)) {
      setLocationData(viewHolder, displayItem);
    } else {
      setPairData(viewHolder, displayItem);
    }
  }

  private void setPairData(GenericViewHolder viewHolder, DisplayItem displayItem) {
    setTextAndShow(viewHolder.tvTitle, displayItem.key);
    setTextAndShow(viewHolder.tvBody, displayItem.displayValue);
  }

  private void setLocationData(GenericViewHolder viewHolder, DisplayItem displayItem) {
    setTextAndShow(viewHolder.tvTitle, displayItem.key);

    viewHolder.ivThumbnail.setVisibility(View.VISIBLE);

    Picasso.with(viewHolder.rootView.getContext())
        .load(displayItem.imageURI)
        .fit()
        .centerCrop()
        .into(viewHolder.ivThumbnail);
  }

  private void setLinkData(GenericViewHolder viewHolder, DisplayItem displayItem) {
    setTextAndShow(viewHolder.tvTitle, displayItem.key);

    if (displayItem.resource instanceof CDAAsset) {
      int size = CFApp.getInstance()
          .getResources()
          .getDimensionPixelSize(R.dimen.entry_preview_image_size);

      Utils.loadThumbnailForAssetWithSize(viewHolder.rootView.getContext(),
          (CDAAsset) displayItem.resource, viewHolder.ivThumbnail, size, size, true);

      viewHolder.ivThumbnail.setVisibility(View.VISIBLE);
    } else {
      setTextAndShow(viewHolder.tvBody, (String) displayItem.resource.getSys().get("id"));
    }
  }

  @Override protected int getItemViewType() {
    return EntryPreviewAdapter.VIEW_TYPE_GENERIC;
  }

  @Override public void reset(GenericViewHolder viewHolder) {
    viewHolder.tvTitle.setText(null);
    viewHolder.tvTitle.setVisibility(View.GONE);

    viewHolder.tvBody.setText(null);
    viewHolder.tvBody.setVisibility(View.GONE);

    viewHolder.ivThumbnail.setImageDrawable(null);
    viewHolder.ivThumbnail.setVisibility(View.GONE);
  }

  private static void setTextAndShow(TextView textView, String text) {
    textView.setText(text);
    textView.setVisibility(View.VISIBLE);
  }
}
