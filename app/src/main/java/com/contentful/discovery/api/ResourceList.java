package com.contentful.discovery.api;

import com.contentful.java.cda.model.CDAContentType;
import com.contentful.java.cda.model.CDAResource;
import java.util.List;
import java.util.Map;

/**
 * Resource List.
 */
public class ResourceList {
  public List<CDAResource> resources;
  public Map<String, CDAContentType> contentTypes;
}
