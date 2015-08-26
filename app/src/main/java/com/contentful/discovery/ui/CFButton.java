package com.contentful.discovery.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;
import com.contentful.discovery.utils.Utils;

public class CFButton extends Button {
  public CFButton(Context context) {
    super(context);
    init(context);
  }

  public CFButton(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CFButton(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    if (!isInEditMode()) {
      setTypeface(Typeface.createFromAsset(context.getAssets(), Utils.FONT_LATO_REGULAR));
    }
  }
}
