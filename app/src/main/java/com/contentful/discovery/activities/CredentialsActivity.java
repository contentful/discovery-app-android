package com.contentful.discovery.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.BounceInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.contentful.discovery.R;
import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.CallbackSet;
import com.contentful.discovery.api.Credentials;
import com.contentful.discovery.fragments.TutorialFragment;
import com.contentful.discovery.services.DBIntentService;
import com.contentful.discovery.utils.AnimHelper;
import com.contentful.discovery.utils.CFPrefs;
import com.contentful.discovery.utils.IntentConsts;
import com.contentful.discovery.utils.Utils;
import com.contentful.discovery.utils.ViewHelper;
import com.contentful.java.api.CDACallback;
import com.contentful.java.model.CDASpace;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Credentials Activity.
 */
public class CredentialsActivity extends CFFragmentActivity {
    private static final int RC_HISTORY = 1;
    private boolean didLogin;

    // Views
    @InjectView(R.id.iv_background) ImageView ivBackground;
    @InjectView(R.id.iv_logo) ImageView ivLogo;
    @InjectView(R.id.btn_secondary) Button btnSecondary;
    @InjectView(R.id.et_space) EditText etSpace;
    @InjectView(R.id.et_token) EditText etToken;
    @InjectView(R.id.btn_go) Button btnGo;

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

        didLogin = CFPrefs.didLogin();

        // Inject views
        ButterKnife.inject(this);

        // Callbacks
        callbacks = new CallbackSet();

        // Background
        initBackground();

        // Logo
        initLogo();

        // Animations
        setupAnimations();

        // Demo / History button
        updateSecondaryButton();

        // Tutorial
        if (CFPrefs.firstLaunch()) {
            CFPrefs.getInstance()
                    .edit()
                    .putBoolean(CFPrefs.KEY_FIRST_LAUNCH, false)
                    .apply();

            Utils.attachTutorialFragment(
                    getSupportFragmentManager(),
                    R.id.tutorial_wrapper,
                    new TutorialFragment());
        }
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
        // Cancel & clear all pending/ongoing requests
        callbacks.cancelAndClear();

        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (!TutorialFragment.handleOnBackPressed(getSupportFragmentManager())) {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_HISTORY) {
            handleHistoryActivityResult(resultCode, data);
        }
    }

    @OnClick(R.id.tv_help)
    void onClickHelp() {
        startActivity(new Intent(this, HelpActivity.class));
    }

    @OnClick(R.id.btn_go)
    void onClickGo() {
        Credentials credentials = getCredentials();

        if (validateCredentials(credentials)) {
            // Credentials pre-validations successful, attempt login.
            login(credentials);
        }
    }

    @OnClick(R.id.btn_secondary)
    void onClickSecondary() {
        if (didLogin) {
            startActivityForResult(new Intent(this, HistoryActivity.class), RC_HISTORY);
        } else {
            etSpace.setText(R.string.demo_space_key);
            etToken.setText(R.string.demo_space_token);

            btnGo.performClick();
        }
    }

    /**
     * Handle Activity result from HistoryActivity
     */
    private void handleHistoryActivityResult(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Credentials credentials = data.getParcelableExtra(IntentConsts.EXTRA_CREDENTIALS);

            etSpace.setText(credentials.getSpace());
            etToken.setText(credentials.getAccessToken());
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
                true,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        callbacks.cancelAndClear();
                    }
                });

        // Initialize client singleton & test our credentials while attempting to fetch
        // their corresponding {@link Space}.
        // Note that we're adding the CDACallback instance to our callbacks set, case we
        // would need to cancel the request in the future (i.e. onDestroy).
        CFClient.init(credentials.getSpace(), credentials.getAccessToken())
                .fetchSpace(callbacks.add(new CDACallback<CDASpace>() {
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

                        updateDidLogin(credentials);

                        // Start Space Activity
                        startActivity(new Intent(CredentialsActivity.this, SpaceActivity.class)
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
                                body = getString(R.string.ad_error_message_generic);
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

    private void updateDidLogin(Credentials credentials) {
        if (!getString(R.string.demo_space_key).equals(credentials.getSpace())) {
            // Update 'didLogin' flag
            CFPrefs.getInstance().edit()
                    .putBoolean(CFPrefs.KEY_DID_LOGIN, true)
                    .apply();

            didLogin = true;

            updateSecondaryButton();
        }
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
     * Creates a set of {@code Credentials} from the UI fields.
     *
     * @return {@code Credentials} instance.
     */
    private Credentials getCredentials() {
        String space = etSpace.getText().toString();
        String token = etToken.getText().toString();

        return new Credentials(null, space, token, null);
    }

    private void updateSecondaryButton() {
        if (didLogin) {
            btnSecondary.setText(R.string.btn_secondary_history);
        } else {
            btnSecondary.setText(R.string.btn_secondary_demo);
        }
    }

    private void initLogo() {
        Picasso.with(this)
                .load(R.drawable.logo_contentful)
                .fit()
                .centerCrop()
                .into(ivLogo);
    }

    private void initBackground() {
        Picasso.with(this)
                .load(R.drawable.bg)
                .fit()
                .centerCrop()
                .into(ivBackground);

        ViewHelper.addGlobalLayoutListener(ivBackground, new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewHelper.removeGlobalLayoutListener(ivBackground, this);

                ViewGroup.LayoutParams lp = ivBackground.getLayoutParams();
                lp.width = ivBackground.getWidth();
                lp.height = ivBackground.getHeight();

                // no need to requestLayout here, the dimensions are explicitly specified to avoid
                // scaling of the background image when adjustResize occurs.
            }
        });
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
