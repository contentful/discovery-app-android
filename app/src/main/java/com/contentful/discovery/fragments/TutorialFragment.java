package com.contentful.discovery.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.contentful.discovery.R;
import com.contentful.discovery.adapters.TutorialAdapter;
import com.contentful.discovery.loaders.TutorialLoader;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.squareup.picasso.Picasso;
import com.viewpagerindicator.CirclePageIndicator;

/**
 * TutorialFragment.
 */
public class TutorialFragment extends Fragment implements TutorialAdapter.Listener {
  @InjectView(R.id.iv_background) ImageView ivBackground;
  @InjectView(R.id.view_pager) ViewPager viewPager;
  @InjectView(R.id.vp_indicator) CirclePageIndicator vpIndicator;
  @InjectView(R.id.video_wrapper) ViewGroup videoWrapper;
  @InjectView(R.id.video_view) VideoView videoView;
  @InjectView(R.id.pb_video) ProgressBar pbVideo;

  private ProgressDialog tutDialog;
  private TutorialAdapter adapter;
  private MediaController mediaController;
  private boolean videoPrepared;

  @Override public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    tutDialog = ProgressDialog.show(getActivity(), getString(R.string.pd_tutorial_title),
        getString(R.string.pd_tutorial_message), true, false);

    adapter = new TutorialAdapter(getActivity());

    getLoaderManager().initLoader(Utils.getLoaderId(this), null,
        new LoaderManager.LoaderCallbacks<TutorialLoader.Tutorial>() {
          @Override public Loader<TutorialLoader.Tutorial> onCreateLoader(int i, Bundle bundle) {
            return new TutorialLoader();
          }

          @Override public void onLoadFinished(Loader<TutorialLoader.Tutorial> tutorialLoader,
              TutorialLoader.Tutorial tutorial) {

            if (tutDialog != null) {
              tutDialog.cancel();
              tutDialog = null;
            }

            if (tutorial == null) {
              Utils.showGenericError(getActivity(), new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                      getActivity().onBackPressed();
                    }
                  });
            } else {
              displayTutorial(tutorial);
            }
          }

          @Override public void onLoaderReset(Loader<TutorialLoader.Tutorial> tutorialLoader) {
            if (tutDialog != null) {
              tutDialog.cancel();
              tutDialog = null;
            }
          }
        });
  }

  @Override public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    return inflater.inflate(R.layout.fragment_tutorial, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    ButterKnife.inject(this, view);
    viewPager.setAdapter(adapter);
    vpIndicator.setViewPager(viewPager);
  }

  @Override public void onPause() {
    hideVideo();
    super.onPause();
  }

  @OnClick(R.id.tv_skip)
  void onClickSkip() {
    getActivity().onBackPressed();
  }

  private void displayTutorial(TutorialLoader.Tutorial tutorial) {
    adapter.setTutorial(tutorial);
    adapter.setListener(this);
    adapter.notifyDataSetChanged();

    // Background image
    Picasso.with(getActivity())
        .load(tutorial.backgroundImageUrl)
        .fit()
        .centerCrop()
        .into(ivBackground);
  }

  @Override public void onVideoClicked() {
    videoView.setVideoURI(Uri.parse("android.resource://"
        + IntentConsts.PACKAGE_NAME + "/" + R.raw.contentful_video));

    if (mediaController == null) {
      MediaController mediaController = new MediaController(getActivity());
      mediaController.setAnchorView(videoView);
      mediaController.setMediaPlayer(videoView);
    }

    videoView.setMediaController(mediaController);

    if (videoPrepared) {
      pbVideo.setVisibility(View.GONE);
    } else {
      pbVideo.setVisibility(View.VISIBLE);

      videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
        @Override public void onPrepared(MediaPlayer mp) {
          pbVideo.setVisibility(View.GONE);
        }
      });
    }

    videoWrapper.setVisibility(View.VISIBLE);
    videoView.start();
  }

  public boolean isVideoShowing() {
    return videoWrapper.getVisibility() == View.VISIBLE;
  }

  public void hideVideo() {
    if (videoView.isPlaying()) {
      videoView.stopPlayback();
    }

    videoView.setOnPreparedListener(null);

    videoWrapper.setVisibility(View.GONE);
  }

  public static boolean handleOnBackPressed(FragmentManager fm) {
    TutorialFragment tutorialFragment =
        (TutorialFragment) fm.findFragmentByTag(TutorialFragment.class.getName());

    if (tutorialFragment != null) {
      if (tutorialFragment.isVideoShowing()) {
        tutorialFragment.hideVideo();
        return true;
      }
    }

    return false;
  }
}
