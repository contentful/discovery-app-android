package discovery.contentful.activities;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import butterknife.Bind;
import butterknife.ButterKnife;
import discovery.contentful.R;
import discovery.contentful.utils.IntentConsts;

public class WebActivity extends CFFragmentActivity {
  @Bind(R.id.web_view) WebView webView;
  @Bind(R.id.empty) View emptyView;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_web);

    // Inject views
    ButterKnife.bind(this);

    webView.setWebViewClient(new WebViewClient() {
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
