package discovery.contentful.loaders;

import discovery.contentful.api.CFClient;
import discovery.contentful.api.ContentTypeWrapper;
import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAResource;
import java.util.ArrayList;
import java.util.List;
import retrofit.RetrofitError;

public class ContentTypesLoader extends AbsAsyncTaskLoader<ArrayList<ContentTypeWrapper>> {
  @Override protected ArrayList<ContentTypeWrapper> performLoad() {
    CDAClient client = CFClient.getClient();

    try {
      List<CDAResource> items = client.fetch(CDAContentType.class).all().items();
      ArrayList<ContentTypeWrapper> tmp = new ArrayList<>();

      if (items.size() > 0) {
        for (CDAResource res : items) {
          CDAContentType cdaContentType = (CDAContentType) res;

          // Entries count
          CDAArray entries = client.fetch(CDAEntry.class)
              .where("content_type", cdaContentType.id())
              .where("limit", "1")
              .all();

          ContentTypeWrapper ct = new ContentTypeWrapper(cdaContentType, entries.total());
          tmp.add(ct);
        }
      }

      return tmp;
    } catch (RetrofitError e) {
      e.printStackTrace();
    }

    return null;
  }
}
