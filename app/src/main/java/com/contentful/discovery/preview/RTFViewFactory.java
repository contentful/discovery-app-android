package com.contentful.discovery.preview;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.activities.TextPreviewActivity;
import com.contentful.discovery.ui.DisplayItem;
import org.apache.commons.lang3.CharEncoding;

/**
 * RTF View Factory.
 */
public class RTFViewFactory extends PreviewViewFactory<RTFViewHolder> {
  @Override protected int getLayoutResId() {
    return R.layout.view_preview_rtf;
  }

  @Override protected int getItemViewType() {
    return EntryPreviewAdapter.VIEW_TYPE_RTF;
  }

  @Override protected RTFViewHolder createViewHolder(Object factoryKey, View v) {
    RTFViewHolder vh = new RTFViewHolder(factoryKey, v);

    vh.webView.setWebViewClient(new WebViewClient() {
      @Override
      public boolean shouldOverrideUrlLoading(WebView view, String url) {
        return true;
      }
    });

    return vh;
  }

  @Override protected void setViewData(RTFViewHolder viewHolder, DisplayItem displayItem) {
    viewHolder.webView.loadDataWithBaseURL(CFApp.getInstance().getString(R.string.url_contentful),
        displayItem.displayValue, TextPreviewActivity.MIME_TYPE_TEXT_HTML, CharEncoding.UTF_8,
        null);

    viewHolder.webView.setBackgroundColor(0);
  }

  @Override public void reset(RTFViewHolder viewHolder) {
    viewHolder.webView.loadUrl("about:blank");
  }
}