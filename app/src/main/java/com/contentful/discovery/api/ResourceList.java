package com.contentful.discovery.api;

import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAResource;
import java.util.List;
import java.util.Map;

public class ResourceList {
  public List<CDAResource> resources;
  public Map<String, CDAContentType> contentTypes;
}
