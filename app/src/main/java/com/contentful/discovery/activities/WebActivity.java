package com.contentful.discovery.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.contentful.discovery.R;
import com.contentful.discovery.utils.IntentConsts;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Web Activity.
 */
public class WebActivity extends CFFragmentActivity {
    @InjectView(R.id.web_view) WebView webView;
    @InjectView(R.id.empty) View emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web);

        // Inject views
        ButterKnife.inject(this);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);

                emptyView.setVisibility(View.GONE);
            }
        });

        webView.loadUrl(getIntent().getStringExtra(IntentConsts.EXTRA_URL));

        emptyView.setVisibility(View.VISIBLE);
    }
}
