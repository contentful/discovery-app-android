package com.contentful.discovery.preview;

import android.view.View;
import android.webkit.WebView;

import com.contentful.discovery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

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
