package com.contentful.discovery.loaders;

import com.contentful.discovery.api.ResourceList;
import com.contentful.java.api.CDAClient;
import com.contentful.java.model.CDAResource;
import java.util.ArrayList;

/**
 * Static Resource Array Loader.
 */
public class ResourceArrayLoader extends AbsResourceListLoader {
  private final ArrayList<Object> resources;

  public ResourceArrayLoader(ArrayList<Object> resources) {
    super();
    this.resources = resources;
  }

  @Override protected ResourceList performLoad(CDAClient client) {
    ResourceList resourceList = new ResourceList();
    resourceList.resources = new ArrayList<CDAResource>();

    for (Object obj : resources) {
      // Filter out any unresolved Links
      if (obj instanceof CDAResource) {
        resourceList.resources.add((CDAResource) obj);
      }
    }

    return resourceList;
  }
}
