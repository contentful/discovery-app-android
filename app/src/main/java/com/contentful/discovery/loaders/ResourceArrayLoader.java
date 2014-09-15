package com.contentful.discovery.loaders;

import com.contentful.discovery.ResourceList;
import com.contentful.java.api.CDAClient;
import com.contentful.java.model.CDAResource;

import java.util.ArrayList;

/**
 * Static Resource Array Loader.
 */
public class ResourceArrayLoader extends AbsResourceListLoader {
    private final ArrayList<CDAResource> resources;

    public ResourceArrayLoader(ArrayList<CDAResource> resources) {
        super();
        this.resources = resources;
    }

    @Override
    protected ResourceList performLoad(CDAClient client) {
        ResourceList resourceList = new ResourceList();
        resourceList.resources = resources;
        return resourceList;
    }
}
