package com.contentful.discovery.preview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.contentful.discovery.ui.DisplayItem;

/**
 * Abstract Preview View Factory.
 */
public abstract class PreviewViewFactory<T extends AbsViewHolder> {
    public T getView(Context context, View convertView, ViewGroup parent, DisplayItem displayItem, Object factoryKey) {
        T viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(getLayoutResId(), parent, false);
            convertView.setTag(viewHolder = createViewHolder(factoryKey, convertView));
        } else {
            viewHolder = (T) convertView.getTag();
        }

        setViewData(viewHolder, displayItem);

        return viewHolder;
    }

    @LayoutRes
    protected abstract int getLayoutResId();

    protected abstract T createViewHolder(Object factoryKey, View v);

    protected abstract void setViewData(T viewHolder, DisplayItem displayItem);

    protected abstract int getItemViewType();

    public abstract void reset(T viewHolder);
}
