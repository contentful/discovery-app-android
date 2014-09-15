package com.contentful.discovery.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.contentful.discovery.R;
import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.CallbackSet;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.events.ChangeCredentialsEvent;
import com.contentful.discovery.events.ReloadHistoryEvent;
import com.contentful.discovery.services.DBIntentService;
import com.contentful.discovery.utils.AnimHelper;
import com.contentful.discovery.utils.CFBusProvider;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.ViewHelper;
import com.contentful.java.api.CDACallback;
import com.contentful.java.model.CDASpace;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnFocusChange;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Credentials Activity.
 * Allows the users to log in to different {@code Space}s by supplying their credentials.
 * <p/>
 * In addition, this allows easy selection of previously supplied credentials by persisting
 * those in a local database.
 */
public class CredentialsActivity extends FragmentActivity {
    // Views
    @InjectView(R.id.wrapper_main) ViewGroup mainWrapper;
    @InjectView(R.id.iv_logo) ImageView ivLogo;
    @InjectView(R.id.wrapper_history) ViewGroup historyWrapper;
    @InjectView(R.id.et_space) EditText etSpace;
    @InjectView(R.id.et_token) EditText etToken;

    // Animations stuff
    private Handler animHandler;
    private ObjectAnimator rotationAnimator;
    private AnimatorSet scaleAnimatorSet;
    private Runnable bounceRunnable;

    // Set of pending request callbacks, for cancelling requests.
    private CallbackSet callbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credentials);

        // Inject views
        ButterKnife.inject(this);

        // Callbacks Set
        callbacks = new CallbackSet();

        // Animations
        setupAnimations();

        // Setup logo
        setupLogo();

        // Event bus
        CFBusProvider.getInstance().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Resume animations
        AnimHelper.startOrResumeAnimator(rotationAnimator);
        postBounceRunnable();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Pause animations
        AnimHelper.pauseOrStopAnimator(rotationAnimator);
        AnimHelper.pauseOrStopAnimator(scaleAnimatorSet);
        animHandler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        // Unregister from event bus
        CFBusProvider.getInstance().unregister(this);

        // Cancel & clear all pending/ongoing requests
        callbacks.cancelAndClear();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (AnimHelper.isRevealingOrVisible(historyWrapper)) {
            // History view is visible, hide it.
            AnimHelper.hideView(historyWrapper);
            return;
        }

        super.onBackPressed();
    }

    @OnClick(R.id.btn_history)
    void onClickHistory() {
        // Show history view
        AnimHelper.toggle(historyWrapper);

        // Request reload history
        CFBusProvider.getInstance().post(new ReloadHistoryEvent());
    }

    @OnClick(R.id.btn_start)
    void onClickStart() {
        Credentials credentials = getCredentials();

        if (validateCredentials(credentials)) {
            // Credentials pre-validations successful, attempt login.
            login(credentials);
        }
    }

    @OnFocusChange({R.id.et_space, R.id.et_token})
    void onFocusChange(EditText view, boolean focused) {
        if (focused) {
            // Clear the contents of this EditText when focused.
            view.setText(null);
        }
    }

    @Subscribe
    public void onChangeCredentials(ChangeCredentialsEvent e) {
        // Populate the selected Credentials
        etSpace.setText(e.credentials.getSpace());
        etToken.setText(e.credentials.getAccessToken());

        // Notify user
        Toast.makeText(this, R.string.credentials_set, Toast.LENGTH_SHORT).show();
    }

    /**
     * Sets up the {@code ImageView} displaying Contentful logo.
     * For portrait orientation, the size of the logo (width & height) would be the remaining height
     * of the main content view, deducting our inflated layout height sum.
     * For landscape orientation, the logo will be hidden.
     */
    private void setupLogo() {
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            ViewHelper.addGlobalLayoutListener(mainWrapper, new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    ViewHelper.removeGlobalLayoutListener(mainWrapper, this);

                    // Calculate available height
                    int availableHeight = ((ViewGroup) mainWrapper.getParent()).getHeight() -
                            mainWrapper.getHeight();

                    int size = Math.min(availableHeight, mainWrapper.getWidth());

                    // Set dimensions
                    ViewGroup.LayoutParams lp = ivLogo.getLayoutParams();
                    lp.width = lp.height = size;
                    ivLogo.requestLayout();

                    // Load Contentful logo
                    Picasso.with(CredentialsActivity.this)
                            .load(R.drawable.logo_contentful)
                            .fit()
                            .centerInside()
                            .into(ivLogo);
                }
            });
        } else {
            ivLogo.setVisibility(View.GONE);
        }
    }

    /**
     * Attempts to log in to {@code Space}, given a set of {@code Credentials}.
     *
     * @param credentials {@code Credentials} instance.
     */
    private void login(final Credentials credentials) {
        // ProgressDialog to be displayed while the request is carried.
        final ProgressDialog loginDialog = ProgressDialog.show(this,
                getString(R.string.pd_login_title),
                getString(R.string.pd_login_message),
                true,
                false);

        // Initialize client singleton & test our credentials while attempting to fetch
        // their corresponding {@link Space}.
        // Note that we're adding the CDACallback instance to our callbacks set, case we
        // would need to cancel the request in the future (i.e. onDestroy).
        CFClient.init(credentials.getSpace(), credentials.getAccessToken())
                .fetchSpace(callbacks.addObject(new CDACallback<CDASpace>() {
                    @Override
                    protected void onSuccess(CDASpace space, Response response) {
                        // Invalidate options menu
                        supportInvalidateOptionsMenu();

                        // Dismiss ProgressDialog
                        loginDialog.dismiss();

                        // Set additional data on Credentials instance
                        credentials.setSpaceName(space.getName());
                        credentials.setLastLogin(System.currentTimeMillis());

                        // Since request was successful, save these Credentials to the database
                        // for future reference via history, using DBIntentService class.
                        startService(
                                DBIntentService.withAction(IntentConsts.DB.ACTION_SAVE_CREDENTIALS)
                                        .putExtra(IntentConsts.EXTRA_CREDENTIALS, credentials));

                        // Start Space Activity
                        startActivity(new Intent(
                                CredentialsActivity.this,
                                SpaceActivity.class)
                                .putExtra(IntentConsts.EXTRA_SPACE, space));
                    }

                    @Override
                    protected void onFailure(RetrofitError retrofitError) {
                        int statusCode = retrofitError.getResponse().getStatus();

                        // Set error message according to HTTP status code
                        String title = getString(R.string.ad_login_error_title);
                        String body;

                        switch (statusCode) {
                            case HttpStatus.SC_UNAUTHORIZED:
                                body = getString(R.string.ad_login_error_message_unauthorized);
                                break;

                            case HttpStatus.SC_NOT_FOUND:
                                body = getString(R.string.ad_login_error_message_not_found);
                                break;

                            default:
                                body = getString(R.string.ad_login_error_message_generic);
                        }

                        loginDialog.dismiss();

                        // Show error AlertDialog
                        new AlertDialog.Builder(CredentialsActivity.this)
                                .setTitle(title)
                                .setMessage(body)
                                .setPositiveButton(R.string.ok, null)
                                .show();
                    }
                }));
    }

    /**
     * Creates a set of {@code Credentials} from the UI fields.
     *
     * @return {@code Credentials} instance.
     */
    private Credentials getCredentials() {
        String space = etSpace.getText().toString();
        String token = etToken.getText().toString();

        return new Credentials(null, space, token, null);
    }

    /**
     * Performs pre-validations on given {@code Credentials}, right now
     * simply checks that those are not empty.
     *
     * @param credentials {@code Credentials} instance to be validated.
     * @return Boolean indicating whether the supplied {@code Credentials} are valid.
     */
    private boolean validateCredentials(Credentials credentials) {
        if (StringUtils.isBlank(credentials.getSpace())) {
            Toast.makeText(this, getString(R.string.toast_invalid_space), Toast.LENGTH_SHORT).show();
            return false;
        }

        if (StringUtils.isBlank(credentials.getAccessToken())) {
            Toast.makeText(this, getString(R.string.toast_invalid_access_token), Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    /**
     * Sets up animations.
     */
    private void setupAnimations() {
        // Animations Handler
        animHandler = new Handler();

        // Rotation animator
        rotationAnimator = ObjectAnimator.ofFloat(ivLogo, "rotation", 0, 360);

        rotationAnimator.setDuration(
                getResources().getInteger(R.integer.anim_logo_rotate_duration_ms));

        rotationAnimator.setRepeatCount(ValueAnimator.INFINITE);
        rotationAnimator.setRepeatMode(ValueAnimator.REVERSE);

        // Bounce Runnable
        bounceRunnable = new Runnable() {
            @Override
            public void run() {
                scaleAnimatorSet.start();
            }
        };

        // Scale AnimatorSet
        scaleAnimatorSet = new AnimatorSet();

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(ivLogo, "scaleX", 1.0f, 1.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(ivLogo, "scaleY", 1.0f, 1.2f, 1.0f);

        scaleX.setInterpolator(new BounceInterpolator());
        scaleY.setInterpolator(new BounceInterpolator());

        scaleAnimatorSet.playTogether(scaleX, scaleY);
        scaleAnimatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                postBounceRunnable();
            }
        });

        scaleAnimatorSet.setDuration(
                getResources().getInteger(R.integer.anim_logo_scale_duration_ms));
    }

    /**
     * Posts bounce {@code Runnable} using a pre-defined delay.
     */
    private void postBounceRunnable() {
        animHandler.postDelayed(bounceRunnable,
                getResources().getInteger(R.integer.anim_logo_bounce_delay_ms));
    }
}
