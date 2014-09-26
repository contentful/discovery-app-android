package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAAsset;
import com.contentful.java.model.CDAResource;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

/**
 * Assets Loader.
 * Use to load all CDA {@code Asset}s from the current {@code Space}.
 */
public class AssetsLoader extends AbsAsyncTaskLoader<ArrayList<CDAAsset>> {
    private static Gson gson;

    public AssetsLoader() {
        super();

        if (gson == null) {
            gson = new GsonBuilder().create();
        }
    }

    @Override
    protected ArrayList<CDAAsset> performLoad() {
        CDAArray cdaArray = CFClient.getClient().fetchAssetsBlocking();
        ArrayList<CDAAsset> tmp = new ArrayList<CDAAsset>();

        for (CDAResource res : cdaArray.getItems()) {
            if (res instanceof CDAAsset) {
                tmp.add((CDAAsset) res);
            }
        }

        return tmp;
    }
}
