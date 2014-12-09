package com.contentful.discovery.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.View;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.fragments.TutorialFragment;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;

/**
 * About Activity.
 */
public class AboutActivity extends CFFragmentActivity {
  @InjectView(R.id.tv_version) TextView tvVersion;

  private static SparseArray<String> sLinks;

  static {
    CFApp context = CFApp.getInstance();

    sLinks = new SparseArray<>();
    sLinks.put(R.id.btn_faq, context.getString(R.string.url_faq));
    sLinks.put(R.id.btn_feedback, context.getString(R.string.url_feedback));
    sLinks.put(R.id.btn_contact, context.getString(R.string.url_contact));
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_about);

    // Inject views
    ButterKnife.inject(this);

    // Set version
    setVersion();
  }

  @Override public void onBackPressed() {
    if (!TutorialFragment.handleOnBackPressed(getSupportFragmentManager())) {
      super.onBackPressed();
    }
  }

  @OnClick({ R.id.btn_faq, R.id.btn_feedback, R.id.btn_contact })
  void onClickWebReference(View v) {
    startActivity(new Intent(this, WebActivity.class)
        .putExtra(IntentConsts.EXTRA_URL, sLinks.get(v.getId())));
  }

  @OnClick(R.id.btn_product_tour)
  void onClickProductTour() {
    Utils.attachTutorialFragment(getSupportFragmentManager(), R.id.tutorial_wrapper,
        new TutorialFragment());
  }

  @OnClick(R.id.btn_licensing)
  void onClickLicensing() {
    startActivity(new Intent(this, WebActivity.class)
        .putExtra(IntentConsts.EXTRA_URL,
            "file:///android_asset/license.html")
        .putExtra(IntentConsts.EXTRA_ALLOW_LINKS, true));
  }

  private void setVersion() {
    try {
      String versionName = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;

      tvVersion.setText(getString(R.string.version_name, versionName));
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }
  }
}
