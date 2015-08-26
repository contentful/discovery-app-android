package com.contentful.discovery.ui;

import android.view.View;
import android.widget.ImageView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.contentful.discovery.R;

public class FieldViewHolder {
  public @Bind(R.id.tv_title) CFTextView tvTitle;
  public @Bind(R.id.tv_value) CFTextView tvValue;
  public @Bind(R.id.iv_arrow) ImageView ivArrow;

  public FieldViewHolder(View v) {
    ButterKnife.bind(this, v);
  }
}
