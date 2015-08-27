package discovery.contentful.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.widget.ImageView;
import discovery.contentful.CFApp;
import discovery.contentful.R;
import discovery.contentful.fragments.TutorialFragment;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Utils {
  private Utils() {
  }

  // Fonts
  public static final String FONT_LATO_REGULAR = "fonts/lato_regular.ttf";
  public static final String FONT_LATO_BOLD = "fonts/lato_bold.ttf";

  // Loaders
  private static final LinkedHashMap<Class, Integer> LOADERS = new LinkedHashMap<>();

  // Mime Types
  private static HashMap<String, String> mimeTypesMap;

  /**
   * Returns a unique Loader ID for a given object.
   *
   * @param obj Object to assign/retrieve the unique ID to/from
   * @return Integer representing a unique ID to be used as a Loader ID.
   */
  public synchronized static int getLoaderId(Object obj) {
    Class<?> clazz = obj.getClass();
    Integer loaderId = LOADERS.get(clazz);

    if (loaderId == null) {
      if (LOADERS.size() == 0) {
        loaderId = 0;
      } else {
        Integer[] arr = LOADERS.values().toArray(new Integer[LOADERS.size()]);
        loaderId = arr[arr.length - 1] + 1;
      }

      LOADERS.put(clazz, loaderId);
    }

    return loaderId;
  }

  /**
   * Gets a Drawable resource ID for a given Asset.
   *
   * @param asset {@code CDAAsset} instance.
   * @return Integer representing the Drawable resource id to be used as a thumbnail for the
   * given {@code CDAAsset}.
   */
  @DrawableRes private static synchronized Integer getThumbnailResIdForAsset(CDAAsset asset) {
    Context context = CFApp.getInstance();

    if (mimeTypesMap == null) {
      loadMimeTypes(context);
    }

    String drawableName = mimeTypesMap.get(asset.mimeType());
    Integer thumbnailResId = null;

    if (drawableName == null) {
      thumbnailResId = R.drawable.attachment;
    } else if (!"image".equals(drawableName)) {
      thumbnailResId = context.getResources()
          .getIdentifier("drawable/" + drawableName, null, context.getPackageName());

      if (thumbnailResId == 0) {
        thumbnailResId = R.drawable.attachment;
      }
    }

    return thumbnailResId;
  }

  /**
   * Populates {@code mimeTypesMap} with a pre-defined list, kept in "raw/file_groups" file.
   */
  private static void loadMimeTypes(Context context) {
    InputStream is = null;

    try {
      is = context.getResources().openRawResource(R.raw.file_groups);
      String data = IOUtils.toString(is);
      JSONObject jsonObject = new JSONObject(data);
      mimeTypesMap = new HashMap<>();

      Iterator<String> keys = jsonObject.keys();

      while (keys.hasNext()) {
        String key = keys.next();

        JSONArray jsonArray = jsonObject.getJSONArray(key);

        for (int i = 0; i < jsonArray.length(); i++) {
          JSONObject item = jsonArray.getJSONObject(i);
          mimeTypesMap.put((String) item.get("type"), key);
        }
      }
    } catch (IOException | JSONException e) {
      e.printStackTrace();
    } finally {
      if (is != null) {
        try {
          is.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  /**
   * Loads a thumbnail of an Asset to a given ImageView.
   * If explicit dimensions are specified ({@code width} and {@code height}) those will be
   * passed as URL parameters to allow server-side scaling of the image.
   */
  public static void loadThumbnailForAssetWithSize(Context context, CDAAsset asset,
      ImageView imageView, @Nullable Integer width, @Nullable Integer height,
      boolean centerCropRemoteImages) {

    Integer thumbnailResId = Utils.getThumbnailResIdForAsset(asset);
    RequestCreator rc;
    Picasso picasso = Picasso.with(context);
    boolean hasExplicitDimensions = width != null && height != null;

    if (thumbnailResId == null) {
      String url = "http:" + asset.url();

      if (hasExplicitDimensions) {
        if (url.contains("?")) {
          url += "&";
        } else {
          url += "?";
        }
        url += "w=" + width + "&h=" + height;
      }

      rc = picasso.load(url);

      if (hasExplicitDimensions) {
        rc.resize(width, height);
      } else {
        rc.fit();
      }

      if (centerCropRemoteImages) {
        rc.centerCrop();
      } else {
        rc.centerInside();
      }
    } else {
      rc = picasso.load(thumbnailResId).fit().centerCrop();
    }

    rc.into(imageView);
  }

  /**
   * Return true in case an Activity component that can be used to handle this Intent is found.
   */
  public static boolean resolveActivity(Intent intent) {
    return intent.resolveActivityInfo(CFApp.getInstance().getPackageManager(), 0) != null;
  }

  /**
   * Sets the title for an Activity if it is attached as extra
   * ({@link discovery.contentful.utils.IntentConsts#EXTRA_TITLE}).
   */
  public static void setTitleFromIntent(Activity activity) {
    Intent intent = activity.getIntent();

    if (intent != null) {
      String title = intent.getStringExtra(IntentConsts.EXTRA_TITLE);

      if (StringUtils.isNotBlank(title)) {
        activity.setTitle(title);
      }
    }
  }

  /**
   * Gets a URI to display a static map given lat & lon coordinates.
   *
   * @param latitude double representing the latitude.
   * @param longitude double representing the longitude.
   * @return String representing the result URI.
   */
  public static String getStaticMapUri(double latitude, double longitude) {
    Context context = CFApp.getInstance();
    int zoom = context.getResources().getInteger(R.integer.map_default_zoom);
    return context.getString(R.string.static_map_uri, zoom, latitude, longitude);
  }

  /**
   * Shows {@code AlertDialog} with a generic message.
   */
  public static void showGenericError(Activity activity,
      DialogInterface.OnClickListener clickListener) {

    new AlertDialog.Builder(activity).setTitle(R.string.ad_error_title_generic)
        .setMessage(R.string.ad_error_message_generic)
        .setPositiveButton(R.string.ok, clickListener)
        .show();
  }

  /**
   * Gets the {@code CDAContentType} for an {@code CDAEntry} instance by it's Content Type ID.
   */
  public static CDAContentType getContentTypeForEntry(Map<String, CDAContentType> contentTypesMap,
      CDAEntry entry) {

    Map contentType = entry.getAttribute("contentType");
    String contentTypeId = (String) ((Map) contentType.get("sys")).get("id");

    return contentTypesMap.get(contentTypeId);
  }

  public static void attachTutorialFragment(FragmentManager fm, @IdRes int container,
      TutorialFragment fragment) {

    fm.beginTransaction()
        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out,
            android.R.anim.fade_in, android.R.anim.fade_out)
        .add(R.id.tutorial_wrapper, fragment, TutorialFragment.class.getName())
        .addToBackStack(null)
        .commit();
  }

  public static String getTitleForEntry(CDAEntry entry, CDAContentType contentType) {
    String displayField = contentType.displayField();

    if (!StringUtils.isBlank(displayField)) {
      String result = entry.getField(displayField);

      if (StringUtils.isNotBlank(result)) {
        return result;
      }
    }

    return CFApp.getInstance().getString(R.string.content_type_default_title);
  }
}
