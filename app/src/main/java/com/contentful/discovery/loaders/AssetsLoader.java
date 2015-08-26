package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.java.cda.CDAAsset;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import retrofit.RetrofitError;

public class AssetsLoader extends AbsAsyncTaskLoader<ArrayList<CDAAsset>> {
  private static Gson gson;

  public AssetsLoader() {
    super();

    if (gson == null) {
      gson = new GsonBuilder().create();
    }
  }

  @Override protected ArrayList<CDAAsset> performLoad() {
    try {
      return new ArrayList<>(CFClient.getClient().fetch(CDAAsset.class).all().assets().values());
    } catch (RetrofitError e) {
      e.printStackTrace();

      return null;
    }
  }
}
