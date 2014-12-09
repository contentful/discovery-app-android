package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ResourceList;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.model.CDAArray;
import com.contentful.java.cda.model.CDAContentType;
import com.contentful.java.cda.model.CDAEntry;
import com.contentful.java.cda.model.CDAResource;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit.RetrofitError;

/**
 * Entries by Content Type Loader.
 * Use to load all CDA Entries matching the given CDAContentType, from the current Space.
 */
public class EntriesByContentTypeLoader extends AbsResourceListLoader {
  private final CDAContentType contentType;

  public EntriesByContentTypeLoader(CDAContentType contentType) {
    super();
    this.contentType = contentType;
  }

  @Override protected ResourceList performLoad(CDAClient client) {
    try {
      ResourceList resourceList = new ResourceList();

      // Prepare query
      HashMap<String, String> query = new HashMap<>();
      query.put("content_type", (String) contentType.getSys().get("id"));

      // Set the locale if non-default locale is currently configured
      String locale = CFClient.getLocale();

      if (locale != null) {
        query.put("locale", locale);
      }

      // Make the request
      CDAArray cdaArray = client.entries().fetchAll(query);
      resourceList.resources = new ArrayList<>();

      // Prepare the result
      for (CDAResource res : cdaArray.getItems()) {
        if (res instanceof CDAEntry) {
          resourceList.resources.add(res);
        }
      }

      return resourceList;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }
}
