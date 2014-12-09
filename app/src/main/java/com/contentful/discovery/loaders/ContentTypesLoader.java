package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ContentTypeWrapper;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.model.CDAArray;
import com.contentful.java.cda.model.CDAContentType;
import com.contentful.java.cda.model.CDAResource;
import java.util.ArrayList;
import java.util.HashMap;
import retrofit.RetrofitError;

/**
 * Content Types Loader.
 * Use to load all CDA {@code Content Type}s from the current {@code Space}.
 */
public class ContentTypesLoader extends AbsAsyncTaskLoader<ArrayList<ContentTypeWrapper>> {
  @Override protected ArrayList<ContentTypeWrapper> performLoad() {
    CDAClient client = CFClient.getClient();

    try {
      ArrayList<CDAResource> items = client.contentTypes().fetchAll().getItems();
      ArrayList<ContentTypeWrapper> tmp = new ArrayList<>();

      if (items.size() > 0) {
        for (CDAResource res : items) {
          CDAContentType cdaContentType = (CDAContentType) res;

          // Entries count
          HashMap<String, String> query = new HashMap<>();
          query.put("content_type", (String) cdaContentType.getSys().get("id"));
          query.put("limit", "1");
          CDAArray entries = client.entries().fetchAll(query);
          ContentTypeWrapper ct = new ContentTypeWrapper(cdaContentType, entries.getTotal());
          tmp.add(ct);
        }
      }

      return tmp;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }
}
