package com.contentful.discovery.preview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.contentful.discovery.R;

public class ArrayViewHolder extends AbsViewHolder {
  @Bind(R.id.wrapper) ViewGroup wrapper;
  @Bind(R.id.tv_title) TextView tvTitle;

  public ArrayViewHolder(Object factoryKey, View rootView) {
    super(factoryKey, rootView);
    ButterKnife.bind(this, rootView);
  }
}
