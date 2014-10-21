package com.contentful.discovery.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import com.contentful.discovery.loaders.TutorialLoader;
import com.contentful.discovery.ui.TutorialView;

/**
 * TutorialAdapter.
 */
public class TutorialAdapter extends PagerAdapter {
  private static final int POS_VIDEO = 0;

  private Context context;
  private TutorialLoader.Tutorial tutorial;
  private Listener listener;

  public interface Listener {
    void onVideoClicked();
  }

  public TutorialAdapter(Context context) {
    this.context = context;
  }

  @Override public int getCount() {
    if (tutorial == null) {
      return 0;
    }

    return tutorial.pages.size();
  }

  @Override public Object instantiateItem(ViewGroup container, int position) {
    TutorialView tutorialView = new TutorialView(context);
    tutorialView.setPage(tutorial.pages.get(position));
    container.addView(tutorialView);

    if (position == POS_VIDEO) {
      tutorialView.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (listener != null) {
            listener.onVideoClicked();
          }
        }
      });
    }

    return tutorialView;
  }

  @Override public boolean isViewFromObject(View view, Object o) {
    return view == o;
  }

  @Override public void destroyItem(ViewGroup container, int position, Object object) {
    container.removeView((View) object);
  }

  public void setTutorial(TutorialLoader.Tutorial tutorial) {
    this.tutorial = tutorial;
  }

  public void setListener(Listener listener) {
    this.listener = listener;
  }
}
