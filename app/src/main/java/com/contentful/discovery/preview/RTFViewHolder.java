package com.contentful.discovery.preview;

import android.view.View;
import android.webkit.WebView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.contentful.discovery.R;

/**
 * Rich Text Format View Holder.
 */
public class RTFViewHolder extends AbsViewHolder {
  @InjectView(R.id.web_view) WebView webView;

  public RTFViewHolder(Object factoryKey, View rootView) {
    super(factoryKey, rootView);
    ButterKnife.inject(this, rootView);
  }
}
