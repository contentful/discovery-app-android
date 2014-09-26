package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ContentTypeWrapper;
import com.contentful.java.api.CDAClient;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAResource;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Content Types Loader.
 * Use to load all CDA {@code Content Type}s from the current {@code Space}.
 */
public class ContentTypesLoader extends AbsAsyncTaskLoader<ArrayList<ContentTypeWrapper>> {
    @Override
    protected ArrayList<ContentTypeWrapper> performLoad() {
        CDAClient client = CFClient.getClient();

        ArrayList<CDAResource> items = client.fetchContentTypesBlocking().getItems();
        ArrayList<ContentTypeWrapper> tmp = new ArrayList<ContentTypeWrapper>();

        if (items.size() > 0) {
            for (CDAResource res : items) {
                CDAContentType cdaContentType = (CDAContentType) res;

                // Entries count
                HashMap<String, String> query = new HashMap<String, String>();
                query.put("content_type", (String) cdaContentType.getSys().get("id"));
                query.put("limit", "1");

                CDAArray entries = client.fetchEntriesMatchingBlocking(query);

                ContentTypeWrapper ct = new ContentTypeWrapper(cdaContentType, entries.getTotal());

                tmp.add(ct);
            }
        }

        return tmp;
    }
}
