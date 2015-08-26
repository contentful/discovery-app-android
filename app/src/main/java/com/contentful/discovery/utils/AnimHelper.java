package com.contentful.discovery.utils;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.os.Build;

public class AnimHelper {
  private AnimHelper() {
  }

  @SuppressLint("NewApi")
  public static void startOrResumeAnimator(Animator animator) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      if (animator.isPaused()) {
        animator.resume();
        return;
      }
    }

    animator.start();
  }

  @SuppressLint("NewApi")
  public static void pauseOrStopAnimator(Animator animator) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
      animator.pause();
    } else {
      animator.cancel();
    }
  }
}
