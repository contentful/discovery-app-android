package discovery.contentful.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

public class AboutLinearLayout extends LinearLayout {
  public AboutLinearLayout(Context context) {
    super(context);
  }

  public AboutLinearLayout(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  public AboutLinearLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  @Override protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    View first = getChildAt(0);
    View second = getChildAt(1);

    int firstHeight = first.getMeasuredHeight();
    int secondHeight = ((ViewGroup) second).getChildAt(0).getMeasuredHeight();
    int totalHeight = getMeasuredHeight();
    int diff = totalHeight - firstHeight - secondHeight;

    if (diff > 0) {
      first.measure(widthMeasureSpec,
          MeasureSpec.makeMeasureSpec(firstHeight + diff, MeasureSpec.EXACTLY));
    }
  }
}
