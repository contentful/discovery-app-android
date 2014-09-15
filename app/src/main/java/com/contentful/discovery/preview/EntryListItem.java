package com.contentful.discovery.preview;

import android.content.Intent;

/**
 * Entry List Item.
 */
class EntryListItem {
    public String title;
    public String value;
    public Intent intent;

    EntryListItem(String title, String value, Intent intent) {
        this.title = title;
        this.value = value;
        this.intent = intent;
    }
}
