package com.contentful.discovery.api;

import com.contentful.java.cda.CDAContentType;

public class ContentTypeWrapper {
  private final CDAContentType contentType;
  private final int entriesCount;

  public ContentTypeWrapper(CDAContentType contentType, int entriesCount) {
    this.contentType = contentType;
    this.entriesCount = entriesCount;
  }

  public CDAContentType getContentType() {
    return contentType;
  }

  public int getEntriesCount() {
    return entriesCount;
  }
}