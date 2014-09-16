package com.contentful.discovery;

import android.view.View;
import android.widget.ImageView;

import com.contentful.discovery.widget.CFTextView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Field View Holder.
 */
public class FieldViewHolder {
    public @InjectView(R.id.tv_title) CFTextView tvTitle;
    public @InjectView(R.id.tv_value) CFTextView tvValue;
    public @InjectView(R.id.iv_arrow) ImageView ivArrow;

    public FieldViewHolder(View v) {
        ButterKnife.inject(this, v);
    }
}
