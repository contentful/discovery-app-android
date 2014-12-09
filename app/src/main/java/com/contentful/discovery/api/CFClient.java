package com.contentful.discovery.api;

import android.content.SharedPreferences;
import com.contentful.discovery.utils.CFPrefs;
import com.contentful.java.cda.CDAClient;
import org.apache.commons.lang3.StringUtils;

/**
 * Client.
 */
public class CFClient {
  private static CDAClient sInstance;
  private static String locale;

  private CFClient() {
  }

  /**
   * Initialize this client.
   *
   * @param space String representing the Space key.
   * @param token String representing the access token required to log in to the Space.
   * @return {@link com.contentful.java.cda.CDAClient} instance.
   */
  public synchronized static CDAClient init(String space, String token) {
    SharedPreferences prefs = CFPrefs.getInstance();

    prefs.edit()
        .putString(CFPrefs.KEY_SPACE, space)
        .putString(CFPrefs.KEY_ACCESS_TOKEN, token)
        .apply();

    sInstance = new CDAClient.Builder().setSpaceKey(space).setAccessToken(token).build();

    locale = null;

    return sInstance;
  }

  public synchronized static CDAClient getClient() {
    if (sInstance == null) {
      SharedPreferences prefs = CFPrefs.getInstance();

      String space = prefs.getString(CFPrefs.KEY_SPACE, null);
      String token = prefs.getString(CFPrefs.KEY_ACCESS_TOKEN, null);

      if (StringUtils.isBlank(space) || StringUtils.isBlank(token)) {
        throw new IllegalStateException("Uninitialized client.");
      }

      return init(space, token);
    }

    return sInstance;
  }

  /**
   * Set the locale for this client.
   *
   * @param locale String representing the Locale code.
   */
  public synchronized static void setLocale(String locale) {
    CFClient.locale = locale;
  }

  /**
   * Gets the current locale defined for this client.
   *
   * @return String representing the current configured locale, null in case the default
   * locale is being used.
   */
  public static String getLocale() {
    return locale;
  }
}
