package discovery.contentful.activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import com.contentful.java.cda.CDAAsset;
import discovery.contentful.R;
import discovery.contentful.adapters.AssetInfoAdapter;
import discovery.contentful.utils.IntentConsts;
import discovery.contentful.utils.Utils;
import org.apache.commons.lang3.StringUtils;

public class AssetPreviewActivity extends CFListActivity {
  private AssetInfoAdapter adapter;
  private CDAAsset asset;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    // Extract arguments from Intent
    asset = (CDAAsset) getIntent().getSerializableExtra(IntentConsts.EXTRA_ASSET);

    // Title
    Utils.setTitleFromIntent(this);

    // Create a new Adapter
    adapter = new AssetInfoAdapter(this, asset);

    // Setup ListView
    setupListView();
  }

  @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
    super.onItemClick(parent, view, position, id);

    if (Boolean.TRUE.equals(view.getTag(R.id.tag_header_view))) {
      // Photo (header view) was clicked
      onPhotoClicked();
    } else if (Boolean.TRUE.equals(view.getTag(R.id.tag_clickable))) {
      // Clickable field clicked
      Pair<String, String> item = adapter.getItem(position - listView.getHeaderViewsCount());

      if (StringUtils.isNotBlank(item.second)) {
        // Field value is not empty, display in Text Preview Activity.
        startActivity(new Intent(this, TextPreviewActivity.class)
            .putExtra(IntentConsts.EXTRA_TITLE, item.first)
            .putExtra(IntentConsts.EXTRA_TEXT, item.second));
      }
    }
  }

  /**
   * {@code ListView} initialization
   */
  private void setupListView() {
    // Create & add header views
    ImageView imageView = new ImageView(this);

    DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
    int screenWidth = displayMetrics.widthPixels;
    int screenHeight = displayMetrics.heightPixels;
    int headerHeight = (int) (screenHeight / 2.0f);

    AbsListView.LayoutParams lp =
        new AbsListView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, headerHeight);

    imageView.setLayoutParams(lp);
    imageView.setTag(R.id.tag_header_view, true);
    listView.addHeaderView(imageView);
    Utils.loadThumbnailForAssetWithSize(this, asset, imageView, screenWidth, headerHeight, false);

    // Set the Adapter
    listView.setAdapter(adapter);
  }

  /**
   * Callback method to be called when the {@code Asset}'s thumbnail (header view) is clicked.
   * This will check if an {@link Intent#ACTION_VIEW} implicit {@code Intent} along with
   * the {@code Asset}'s URL can be resolved by any application on the system, and if so will
   * fire the {@code Intent}, otherwise will fallback to display an {@code AlertDialog}
   * asking the user whether or not to attempt and open it without enforcing the {@code Asset}'s
   * mime-type (i.e. using their web browser).
   */
  void onPhotoClicked() {
    final Uri uri = Uri.parse("http:" + asset.url());

    Intent intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(uri, asset.mimeType());

    if (Utils.resolveActivity(intent)) {
      startActivity(intent);
    } else {
      new AlertDialog.Builder(this).setTitle(R.string.ad_unknown_file_title)
          .setMessage(R.string.ad_unknown_file_message)
          .setCancelable(true)
          .setNegativeButton(R.string.download, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialog, int which) {
              download(uri);
            }
          })
          .setPositiveButton(R.string.open_in_browser, new DialogInterface.OnClickListener() {
            @Override public void onClick(DialogInterface dialogInterface, int i) {
              startActivity(new Intent(Intent.ACTION_VIEW, uri));
            }
          })
          .show();
    }
  }

  private void download(Uri uri) {
    DownloadManager dm = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
    DownloadManager.Request request = new DownloadManager.Request(uri);
    request.allowScanningByMediaScanner();
    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
    request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,
        uri.getLastPathSegment());
    dm.enqueue(request);
  }
}
