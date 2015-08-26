package com.contentful.discovery.loaders;

import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.api.CFDiscoveryClient;
import com.contentful.java.cda.CDAAsset;
import com.contentful.java.cda.CDAEntry;
import java.util.ArrayList;
import retrofit.RetrofitError;

public class TutorialLoader extends AbsAsyncTaskLoader<TutorialLoader.Tutorial> {
  @Override protected Tutorial performLoad() {
    try {
      Tutorial tmp = new Tutorial();
      CDAEntry entry = CFDiscoveryClient.getClient().fetch(CDAEntry.class)
          .one(CFApp.getInstance().getResources().getString(R.string.discovery_space_tutorial_id));

      CDAAsset bgAsset = entry.getField("backgroundImageIPad");

      // Background image
      tmp.backgroundImageUrl = "http:" + bgAsset.url();

      // Pages
      tmp.pages = new ArrayList<>();
      ArrayList<?> pages = entry.getField("pages");
      for (Object p : pages) {
        if (p instanceof CDAEntry) {
          CDAEntry pageEntry = (CDAEntry) p;
          tmp.pages.add(getPageForEntry(pageEntry));
        }
      }

      return tmp;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }

  private static Tutorial.Page getPageForEntry(CDAEntry entry) {
    Tutorial.Page page = new Tutorial.Page();
    page.headline = entry.getField("headline");
    page.content = entry.getField("content");
    page.asset = entry.getField("asset");
    return page;
  }

  public static class Tutorial {
    public String backgroundImageUrl;
    public ArrayList<Page> pages;

    public static class Page {
      public String headline;
      public String content;
      public CDAAsset asset;
    }
  }
}
