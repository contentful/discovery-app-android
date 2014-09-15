package com.contentful.discovery;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Field View Holder.
 */
public class FieldViewHolder {
    public @InjectView(R.id.tv_title) TextView tvTitle;
    public @InjectView(R.id.tv_value) TextView tvValue;
    public @InjectView(R.id.iv_arrow) ImageView ivArrow;

    public FieldViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
