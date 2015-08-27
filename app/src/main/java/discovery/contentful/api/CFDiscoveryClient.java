package discovery.contentful.api;

import android.content.Context;
import discovery.contentful.CFApp;
import discovery.contentful.R;
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
