package discovery.contentful.loaders;

import com.contentful.java.cda.CDAArray;
import com.contentful.java.cda.CDAClient;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.FetchQuery;
import discovery.contentful.api.CFClient;
import discovery.contentful.api.ResourceList;
import java.util.ArrayList;

public class EntriesByContentTypeLoader extends AbsResourceListLoader {
  private final CDAContentType contentType;

  public EntriesByContentTypeLoader(CDAContentType contentType) {
    super();
    this.contentType = contentType;
  }

  @Override protected ResourceList performLoad(CDAClient client) {
    ResourceList resourceList = new ResourceList();

    FetchQuery<CDAEntry> query =
        client.fetch(CDAEntry.class).where("content_type", contentType.id());

    // Set the locale if non-default locale is currently configured
    String locale = CFClient.getLocale();
    if (locale != null) {
      query.where("locale", locale);
    }

    // Make the request
    CDAArray cdaArray = query.all();
    resourceList.resources = new ArrayList<>();

    // Prepare the result
    resourceList.resources.addAll(cdaArray.entries().values());
    return resourceList;
  }
}
