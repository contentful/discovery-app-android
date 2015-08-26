package com.contentful.discovery.ui;

import com.contentful.java.cda.CDAResource;
import com.google.android.gms.maps.model.LatLng;
import java.util.List;

public class DisplayItem {
  public String key;
  public String displayValue;
  public String fieldType;
  public CDAResource resource;
  public String imageURI;
  public LatLng location;

  // Array
  public List<Object> array;
  public String arrayItemType;
  public String arrayLinkType;
}
