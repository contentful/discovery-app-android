package com.contentful.discovery.ui;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import com.contentful.discovery.utils.Utils;

/**
 * CF EditText.
 */
public class CFEditText extends EditText {
  public CFEditText(Context context) {
    super(context);
    init(context);
  }

  public CFEditText(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(context);
  }

  public CFEditText(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context);
  }

  private void init(Context context) {
    if (!isInEditMode()) {
      setTypeface(Typeface.createFromAsset(context.getAssets(), Utils.FONT_LATO_REGULAR));
    }
  }
}
