package com.contentful.discovery.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

/**
 * Clearable EditText.
 */
public class ClearableEditText extends EditText {
    public ClearableEditText(Context context) {
        super(context);
    }

    public ClearableEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ClearableEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onEditorAction(int actionCode) {
        super.onEditorAction(actionCode);

        if (actionCode == EditorInfo.IME_ACTION_DONE) {
            post(new Runnable() {
                @Override
                public void run() {
                    clearFocus();

                    ViewParent parent = getParent();

                    if (parent != null && parent instanceof View) {
                        ((View) parent).requestFocus();
                    }
                }
            });
        }
    }

    @Override
    public boolean dispatchKeyEventPreIme(@NonNull KeyEvent event) {
        if (!super.dispatchKeyEventPreIme(event)) {
            int keyCode = event.getKeyCode();

            if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_4) {
                clearFocus();
            }

            return false;
        }

        return true;
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);

        if (!hasWindowFocus) {
            clearFocus();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        clearFocus();
        super.onDetachedFromWindow();
    }
}
