package com.contentful.discovery.preview;

import android.view.View;

abstract class AbsViewHolder {
  public final View rootView;
  public final Object factoryKey;

  AbsViewHolder(Object factoryKey, View rootView) {
    this.factoryKey = factoryKey;
    this.rootView = rootView;
  }
}
