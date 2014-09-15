package com.contentful.discovery.preview;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.contentful.discovery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Array View Holder.
 */
public class ArrayViewHolder extends AbsViewHolder {
    @InjectView(R.id.wrapper) ViewGroup wrapper;
    @InjectView(R.id.tv_title) TextView tvTitle;

    public ArrayViewHolder(Object factoryKey, View rootView) {
        super(factoryKey, rootView);
        ButterKnife.inject(this, rootView);
    }
}
