package com.contentful.discovery.api;

import android.content.Context;
import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.java.cda.CDAClient;

public class CFDiscoveryClient {
  private static CDAClient sInstance;

  private CFDiscoveryClient() {
  }

  public static CDAClient getClient() {
    if (sInstance == null) {
      Context context = CFApp.getInstance();

      sInstance =
          CDAClient.builder().setSpace(context.getString(R.string.discovery_space_key))
              .setToken(context.getString(R.string.discovery_space_token))
              .build();
    }

    return sInstance;
  }
}
