package com.contentful.discovery.preview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.contentful.discovery.R;

public class GenericViewHolder extends AbsViewHolder {
  @InjectView(R.id.tv_title) TextView tvTitle;
  @InjectView(R.id.tv_body) TextView tvBody;
  @InjectView(R.id.iv_thumbnail) ImageView ivThumbnail;

  public GenericViewHolder(Object factoryKey, View rootView) {
    super(factoryKey, rootView);
    ButterKnife.inject(this, rootView);
  }
}
