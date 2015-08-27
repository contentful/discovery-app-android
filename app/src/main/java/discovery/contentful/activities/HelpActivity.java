package discovery.contentful.activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import discovery.contentful.R;
import discovery.contentful.api.CFDiscoveryClient;
import discovery.contentful.api.CallbackSet;
import discovery.contentful.ui.CFTextView;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDACallback;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import com.squareup.picasso.Picasso;
import java.util.List;

public class HelpActivity extends CFFragmentActivity {
  @Bind(R.id.container) ViewGroup container;
  @Bind(R.id.empty) View emptyView;
  @BindDimen(R.dimen.help_title_font_size) int helpTitleFontSize;

  private CallbackSet callbacks;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_help);

    // Inject views
    ButterKnife.bind(this);

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

    callbacks.add(CFDiscoveryClient.getClient()
        .fetch(CDAEntry.class)
        .one(getString(R.string.discovery_space_entry_id), new CDACallback<CDAEntry>() {
          @Override
          @SuppressWarnings("unchecked")
          protected void onSuccess(CDAEntry entry) {
            insertTitle((String) entry.getField("title"));

            //noinspection unchecked
            for (CDAResource helpItem : (List<CDAResource>) entry.getField("helpItems")) {
              insertHelpItem((CDAEntry) helpItem);
            }

            emptyView.setVisibility(View.GONE);
            container.setVisibility(View.VISIBLE);
          }
        }));
  }

  class HelpItemViewHolder {
    @Bind(R.id.tv_text) TextView tvText;
    @Bind(R.id.iv_photo) ImageView ivPhoto;
    View rootView;

    HelpItemViewHolder(View v) {
      this.rootView = v;
      ButterKnife.bind(this, v);
    }
  }

  private void insertHelpItem(CDAEntry helpItemEntry) {
    HelpItemViewHolder vh =
        new HelpItemViewHolder(View.inflate(this, R.layout.view_help_item, null));

    // Text
    vh.tvText.setText((String) helpItemEntry.getField("text"));

    // Image
    CDAAsset asset = helpItemEntry.getField("image");

    Picasso.with(this).load("http:" + asset.url())
        .fit()
        .centerInside()
        .into(vh.ivPhoto);

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
    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, helpTitleFontSize);
    container.addView(textView);
  }
}
