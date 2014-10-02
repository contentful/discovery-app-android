package com.contentful.discovery.ui;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.FrameLayout;

import com.contentful.discovery.R;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * AbsListContainer.
 */
public abstract class AbsListContainer<T extends AbsListView> extends FrameLayout {
    @InjectView(R.id.empty) ViewGroup emptyView;
    @InjectView(R.id.extra_views_wrapper) ViewGroup extraViewsWrapper;
    T list;
    Listener listener;

    public interface Listener {
        void retryLoad();
    }

    public AbsListContainer(Context context) {
        super(context);
        init();
    }

    public AbsListContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AbsListContainer(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.view_list_container, this);
        ButterKnife.inject(this);

        addView(list = inflateList(), 0);

        // Empty view
        list.setEmptyView(emptyView);
    }

    @OnClick(R.id.network_error)
    void onClickNetworkError() {
        if (listener != null) {
            hideExtraViews();
            listener.retryLoad();
        }
    }

    protected abstract T inflateList();

    public T getList() {
        return list;
    }

    public void showExtraView(@IdRes int id) {
        hideExtraViewsExcluding(id).setVisibility(View.VISIBLE);
    }

    public void hideExtraViews() {
        hideExtraViewsExcluding(null);
    }

    private View hideExtraViewsExcluding(@Nullable @IdRes Integer id) {
        View result = null;

        for (int i = 0; i < extraViewsWrapper.getChildCount(); i++) {
            View child = extraViewsWrapper.getChildAt(i);

            if (id == null) {
                child.setVisibility(View.GONE);
            } else if (id.equals(child.getId())) {
                result = child;
            } else {
                child.setVisibility(View.GONE);
            }
        }

        return result;
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }
}
