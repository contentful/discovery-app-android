package com.contentful.discovery.preview;

import android.content.Intent;

/**
 * Entry List Item.
 */
class EntryListItem {
    public final String title;
    public final String value;
    public final Intent intent;

    EntryListItem(String title, String value, Intent intent) {
        this.title = title;
        this.value = value;
        this.intent = intent;
    }
}
