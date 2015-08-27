package discovery.contentful.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import butterknife.BindDimen;
import butterknife.ButterKnife;
import discovery.contentful.R;

public class MaxWidthFrameLayout extends FrameLayout {
  @BindDimen(R.dimen.global_max_width) int maxWidth;

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
    ButterKnife.bind(this);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int measuredWidth = MeasureSpec.getSize(widthMeasureSpec);

    if (maxWidth > 0 && maxWidth < measuredWidth) {
      int measureMode = MeasureSpec.getMode(widthMeasureSpec);
      widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, measureMode);
    }

    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }
}
