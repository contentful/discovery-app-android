package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ResourceList;
import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAResource;
import java.util.HashMap;
import retrofit.RetrofitError;

public abstract class AbsResourceListLoader extends AbsAsyncTaskLoader<ResourceList> {
  @Override protected ResourceList performLoad() {
    CDAClient client = CFClient.getClient();

    try {
      ResourceList tmp = performLoad(client);
      setContentTypes(client, tmp);
      return tmp;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }

  protected abstract ResourceList performLoad(CDAClient client);

  protected void setContentTypes(CDAClient client, ResourceList resourceList) throws RetrofitError {
    HashMap<String, CDAContentType> map = new HashMap<>();
    CDAArray array = client.fetch(CDAContentType.class).all();

    for (CDAResource res : array.items()) {
      map.put(res.id(), (CDAContentType) res);
    }

    resourceList.contentTypes = map;
  }
}
