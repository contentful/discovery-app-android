package discovery.contentful.preview;

import android.view.View;
import android.widget.TextView;
import discovery.contentful.CFApp;
import discovery.contentful.R;
import discovery.contentful.ui.DisplayItem;
import discovery.contentful.utils.Utils;
import com.contentful.java.cda.CDAAsset;
import com.squareup.picasso.Picasso;

public class GenericViewFactory extends PreviewViewFactory<GenericViewHolder> {
  @Override protected int getLayoutResId() {
    return R.layout.view_preview_item;
  }

  @Override protected GenericViewHolder createViewHolder(Object factoryKey, View v) {
    return new GenericViewHolder(factoryKey, v);
  }

  @Override protected void setViewData(GenericViewHolder viewHolder, DisplayItem displayItem) {
    if ("Link".equals(displayItem.fieldType)) {
      setLinkData(viewHolder, displayItem);
    } else if ("Location".equals(displayItem.fieldType)) {
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
      setTextAndShow(viewHolder.tvBody, displayItem.resource.id());
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
