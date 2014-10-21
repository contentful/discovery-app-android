package com.contentful.discovery.ui;

import com.contentful.java.lib.Constants;
import com.contentful.java.model.CDAResource;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

/**
 * Display Item.
 */
public class DisplayItem {
  public String key;
  public String displayValue;
  public Constants.CDAFieldType fieldType;
  public CDAResource resource;
  public String imageURI;
  public LatLng location;

  // Array
  public List<Object> array;
  public Constants.CDAFieldType arrayItemType;
  public Constants.CDAResourceType arrayLinkType;
}
