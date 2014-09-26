package com.contentful.discovery.loaders;

import com.contentful.discovery.CFApp;
import com.contentful.discovery.R;
import com.contentful.discovery.api.CFDiscoveryClient;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAAsset;
import com.contentful.java.model.CDAEntry;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * TutorialLoader.
 */
public class TutorialLoader extends AbsAsyncTaskLoader<TutorialLoader.Tutorial> {
    @Override
    protected Tutorial performLoad() {
        Tutorial tmp = new Tutorial();
        HashMap<String, String> query = new HashMap<String, String>();

        query.put("sys.id", CFApp.getInstance()
                .getResources()
                .getString(R.string.discovery_space_tutorial_id));

        CDAArray array = CFDiscoveryClient.getClient().fetchEntriesMatchingBlocking(query);

        CDAEntry entry = (CDAEntry) array.getItems().get(0);
        CDAAsset bgAsset = (CDAAsset) entry.getFields().get("backgroundImageIPad");

        // Background image
        tmp.backgroundImageUrl = bgAsset.getUrl();

        // Pages
        tmp.pages = new ArrayList<Tutorial.Page>();
        ArrayList<?> pages = (ArrayList<?>) entry.getFields().get("pages");
        for (Object p : pages) {
            if (p instanceof CDAEntry) {
                CDAEntry pageEntry = (CDAEntry) p;
                tmp.pages.add(getPageForEntry(pageEntry));
            }
        }

        return tmp;
    }

    private static Tutorial.Page getPageForEntry(CDAEntry entry) {
        Tutorial.Page page = new Tutorial.Page();
        page.headline = (String) entry.getFields().get("headline");
        page.content = (String) entry.getFields().get("content");
        page.asset = (CDAAsset) entry.getFields().get("asset");

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
