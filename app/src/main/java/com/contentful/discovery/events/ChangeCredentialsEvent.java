package com.contentful.discovery.events;

import com.contentful.discovery.api.Credentials;

/**
 * Change Credentials Event.
 * This should be fired once the user has selected a set of Credentials, i.e. from
 * history view.
 */
public class ChangeCredentialsEvent {
    public final Credentials credentials;

    public ChangeCredentialsEvent(Credentials credentials) {
        this.credentials = credentials;
    }
}
