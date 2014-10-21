package com.contentful.discovery.api;

import android.content.Context;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.java.api.CDAClient;

/**
 * Shared Client to Demo Space.
 */
public class CFDiscoveryClient {
  private static CDAClient sInstance;

  private CFDiscoveryClient() {
  }

  public static CDAClient getClient() {
    if (sInstance == null) {
      Context context = CFApp.getInstance();

      sInstance =
          new CDAClient.Builder().setSpaceKey(context.getString(R.string.discovery_space_key))
              .setAccessToken(context.getString(R.string.discovery_space_token))
              .build();
    }

    return sInstance;
  }
}
