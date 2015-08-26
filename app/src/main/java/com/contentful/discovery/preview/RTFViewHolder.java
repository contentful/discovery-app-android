package com.contentful.discovery.preview;

import android.view.View;
import android.webkit.WebView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.contentful.discovery.R;

public class RTFViewHolder extends AbsViewHolder {
  @Bind(R.id.web_view) WebView webView;

  public RTFViewHolder(Object factoryKey, View rootView) {
    super(factoryKey, rootView);
    ButterKnife.bind(this, rootView);
  }
}
