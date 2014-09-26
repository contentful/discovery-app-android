package com.contentful.discovery.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import com.contentful.discovery.utils.Utils;

/**
 * CF TextView.
 */
public class CFTextView extends TextView {
    public CFTextView(Context context) {
        super(context);
        init(context);
    }

    public CFTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public CFTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        if (!isInEditMode()) {
            Typeface typeface = getTypeface();

            setFont(typeface != null && typeface.isBold());
        }
    }

    public void setFont(boolean bold) {
        if (bold) {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), Utils.FONT_LATO_BOLD));
        } else {
            setTypeface(Typeface.createFromAsset(getContext().getAssets(), Utils.FONT_LATO_REGULAR));
        }
    }
}
