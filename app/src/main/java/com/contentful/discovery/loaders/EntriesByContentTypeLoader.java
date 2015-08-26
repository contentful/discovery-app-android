package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ResourceList;
import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.FetchQuery;
import java.util.ArrayList;
import retrofit.RetrofitError;

public class EntriesByContentTypeLoader extends AbsResourceListLoader {
  private final CDAContentType contentType;

  public EntriesByContentTypeLoader(CDAContentType contentType) {
    super();
    this.contentType = contentType;
  }

  @Override protected ResourceList performLoad(CDAClient client) {
    try {
      ResourceList resourceList = new ResourceList();

      FetchQuery<CDAEntry> query = client.fetch(CDAEntry.class)
          .where("content_type", contentType.id());

      // Set the locale if non-default locale is currently configured
      String locale = CFClient.getLocale();
      if (locale != null) {
        query.where("locale", locale);
      }

      // Make the request
      CDAArray cdaArray = query.all();
      resourceList.resources = new ArrayList<>();

      // Prepare the result
      resourceList.resources.addAll(cdaArray.entries().values());
      return resourceList;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }
}
