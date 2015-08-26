package com.contentful.discovery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.contentful.discovery.R;
import com.contentful.discovery.loaders.TutorialLoader;
import com.squareup.picasso.Picasso;

public class TutorialView extends LinearLayout {
  @InjectView(R.id.tv_headline) TextView tvHeadline;
  @InjectView(R.id.iv_photo) ImageView ivPhoto;
  @InjectView(R.id.tv_content) TextView tvContent;

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

    int p = getResources().getDimensionPixelSize(R.dimen.view_tutorial_padding);
    setPadding(p, 0, p, p);

    View.inflate(context, R.layout.view_tutorial, this);

    ButterKnife.inject(this);
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
