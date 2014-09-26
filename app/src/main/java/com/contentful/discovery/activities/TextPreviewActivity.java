package com.contentful.discovery.activities;

import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.contentful.discovery.R;
import com.contentful.discovery.loaders.TextProcessorLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

import org.apache.commons.lang3.CharEncoding;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Text Preview Activity.
 * Displays text inside a {@code WebView}, supporting Markdown.
 */
public class TextPreviewActivity extends CFFragmentActivity implements
        LoaderManager.LoaderCallbacks<String> {

    public static final String MIME_TYPE_TEXT_HTML = "text/html";

    // Views
    @InjectView(R.id.web_view) WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_text_preview);

        // Inject views
        ButterKnife.inject(this);

        // Setup WebView
        if (!getIntent().getBooleanExtra(IntentConsts.EXTRA_ALLOW_LINKS, false)) {
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return true;
                }
            });
        }

        // Title
        Utils.setTitleFromIntent(this);

        // Init Loader
        getSupportLoaderManager().initLoader(Utils.getLoaderId(this), null, this);
    }

    @Override
    protected void onDestroy() {
        webView.loadUrl("about:blank");

        super.onDestroy();
    }


    @Override
    public Loader<String> onCreateLoader(int id, Bundle args) {
        return new TextProcessorLoader(getIntent().getStringExtra(IntentConsts.EXTRA_TEXT));
    }

    @Override
    public void onLoadFinished(Loader<String> loader, String data) {
        webView.loadDataWithBaseURL(
                getString(R.string.url_contentful),
                data,
                MIME_TYPE_TEXT_HTML,
                CharEncoding.UTF_8,
                null);

        webView.setBackgroundColor(0);
    }

    @Override
    public void onLoaderReset(Loader<String> loader) {

    }
}
