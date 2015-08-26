package com.contentful.discovery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import com.contentful.discovery.R;
import com.contentful.discovery.loaders.TutorialLoader;
import com.squareup.picasso.Picasso;

public class TutorialView extends LinearLayout {
  @Bind(R.id.tv_headline) TextView tvHeadline;
  @Bind(R.id.iv_photo) ImageView ivPhoto;
  @Bind(R.id.tv_content) TextView tvContent;
  @BindDimen(R.dimen.view_tutorial_padding) int padding;

  public TutorialView(Context context) {
    super(context);
    init(context);
  }

  public TutorialView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public TutorialView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    setOrientation(VERTICAL);
    View.inflate(context, R.layout.view_tutorial, this);
    ButterKnife.bind(this);
    setPadding(padding, 0, padding, padding);
  }

  public void setPage(TutorialLoader.Tutorial.Page page) {
    // Headline
    tvHeadline.setText(page.headline);

    // Photo
    Picasso.with(getContext())
        .load("http:" + page.asset.url())
        .fit()
        .centerInside()
        .into(ivPhoto);

    // Content
    tvContent.setText(page.content.trim());
  }
}
