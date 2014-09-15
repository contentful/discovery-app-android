package com.contentful.discovery.utils;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.os.Build;
import android.view.View;

import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;

/**
 * Animations Helper.
 */
public class AnimHelper {
    private static final String TAG_VISIBLE = "TAG_VISIBLE";

    /**
     * Reveals a view in an animated fashion.
     */
    public static void revealView(final View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0, 1f);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.setAlpha(0.0f);
                view.setVisibility(View.VISIBLE);
            }
        });

        anim.setDuration(CFApp.getInstance()
                .getResources()
                .getInteger(R.integer.anim_reveal_pre_l_duration_ms));

        anim.start();
        view.setTag(TAG_VISIBLE);
    }

    /**
     * Hides a view in an animated fashion.
     */
    public static void hideView(final View view) {
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", 0);

        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.INVISIBLE);
            }
        });

        anim.setDuration(CFApp.getInstance()
                .getResources()
                .getInteger(R.integer.anim_reveal_pre_l_duration_ms));

        anim.start();
        view.setTag(null);
    }

    /**
     * Calls either {@link #revealView(android.view.View)} or {@link #hideView(android.view.View)} on
     * a view, according to it's current state.
     * State is monitored using tags.
     */
    public static void toggle(View view) {
        if (isRevealingOrVisible(view)) {
            hideView(view);
        } else {
            revealView(view);
        }
    }

    public static boolean isRevealingOrVisible(View view) {
        return TAG_VISIBLE.equals(view.getTag());
    }

    public static void startOrResumeAnimator(Animator animator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (animator.isPaused()) {
                animator.resume();
                return;
            }
        }

        animator.start();
    }

    public static void pauseOrStopAnimator(Animator animator) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            animator.pause();
        } else {
            animator.cancel();
        }
    }
}
