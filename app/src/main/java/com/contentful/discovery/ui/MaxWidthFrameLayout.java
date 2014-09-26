package com.contentful.discovery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.contentful.discovery.R;

/**
 * MaxWidthFrameLayout.
 */
public class MaxWidthFrameLayout extends FrameLayout {
    private int maxWidth;

    public MaxWidthFrameLayout(Context context) {
        super(context);
        init(context);
    }

    public MaxWidthFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MaxWidthFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        maxWidth = getResources().getDimensionPixelSize(R.dimen.global_max_width);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

        if (maxWidth > 0 && maxWidth < measuredWidth) {
            int measureMode = MeasureSpec.getMode(widthMeasureSpec);
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, measureMode);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
