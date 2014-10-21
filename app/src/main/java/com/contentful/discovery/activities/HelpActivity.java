package com.contentful.discovery.activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.contentful.discovery.R;
import com.contentful.discovery.api.CFDiscoveryClient;
import com.contentful.discovery.api.CallbackSet;
import com.contentful.discovery.ui.CFTextView;
import com.contentful.java.api.CDACallback;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAAsset;
import com.contentful.java.model.CDAEntry;
import com.contentful.java.model.CDAResource;
import com.squareup.picasso.Picasso;
import java.util.HashMap;
import java.util.List;
import retrofit.client.Response;

/**
 * Help Activity.
 */
public class HelpActivity extends CFFragmentActivity {
  @InjectView(R.id.container) ViewGroup container;
  @InjectView(R.id.empty) View emptyView;

  private CallbackSet callbacks;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_help);

    // Inject views
    ButterKnife.inject(this);

    // Callbacks
    callbacks = new CallbackSet();

    // Load data
    loadData();
  }

  @Override protected void onDestroy() {
    callbacks.cancelAndClear();

    super.onDestroy();
  }

  private void loadData() {
    emptyView.setVisibility(View.VISIBLE);

    HashMap<String, String> query = new HashMap<String, String>();
    query.put("sys.id", getString(R.string.discovery_space_entry_id));

    CFDiscoveryClient.getClient()
        .fetchEntriesMatching(query, callbacks.add(new CDACallback<CDAArray>() {
              @SuppressWarnings("unchecked")
              @Override
              protected void onSuccess(CDAArray array, Response response) {
                for (CDAResource res : array.getItems()) {
                  CDAEntry entry = (CDAEntry) res;

                  insertTitle((String) entry.getFields().get("title"));

                  for (CDAResource childRes : (List<CDAResource>) entry.getFields()
                      .get("helpItems")) {
                    insertHelpItem((CDAEntry) childRes);
                  }
                }

                emptyView.setVisibility(View.GONE);
                container.setVisibility(View.VISIBLE);
              }
            }));
  }

  class HelpItemViewHolder {
    @InjectView(R.id.tv_text) TextView tvText;
    @InjectView(R.id.iv_photo) ImageView ivPhoto;
    View rootView;

    HelpItemViewHolder(View v) {
      this.rootView = v;
      ButterKnife.inject(this, v);
    }
  }

  private void insertHelpItem(CDAEntry helpItemEntry) {
    HelpItemViewHolder vh =
        new HelpItemViewHolder(View.inflate(this, R.layout.view_help_item, null));

    // Text
    vh.tvText.setText((String) helpItemEntry.getFields().get("text"));

    // Image
    CDAAsset asset = (CDAAsset) helpItemEntry.getFields().get("image");

    Picasso.with(this).load(asset.getUrl()).fit().centerInside().into(vh.ivPhoto);

    container.addView(vh.rootView);
  }

  private CFTextView getTitleView(String text) {
    CFTextView textView = new CFTextView(this);
    textView.setText(text);
    return textView;
  }

  private void insertTitle(String text) {
    CFTextView textView = getTitleView(text);
    textView.setFont(true);

    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX,
        getResources().getDimensionPixelSize(R.dimen.help_title_font_size));

    container.addView(textView);
  }
}
