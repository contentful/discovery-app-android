package com.contentful.discovery.loaders;

import com.contentful.discovery.api.CFClient;
import com.contentful.discovery.api.ResourceList;
import com.contentful.java.api.CDAClient;
import com.contentful.java.model.CDAArray;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;

/**
 * Abstract Resource List Loader.
 */
public abstract class AbsResourceListLoader extends AbsAsyncTaskLoader<ResourceList> {
    @Override
    protected ResourceList performLoad() {
        CDAClient client = CFClient.getClient();

        try {
            ResourceList tmp = performLoad(client);
            setContentTypes(client, tmp);
            return tmp;
        } catch (RetrofitError e) {
            e.printStackTrace();
        }

        return null;
    }

    protected abstract ResourceList performLoad(CDAClient client);

    protected void setContentTypes(CDAClient client, ResourceList resourceList) throws RetrofitError {
        HashMap<String, CDAContentType> map = new HashMap<String, CDAContentType>();
        CDAArray array = client.fetchContentTypesBlocking();

        for (CDAResource res : array.getItems()) {
            CDAContentType contentType = (CDAContentType) res;

            // Exclude disabled fields
            List<Map> fields = contentType.getFields();
            ArrayList<Map> filteredFields = new ArrayList<Map>();

            for (Map f : fields) {
                if (Boolean.TRUE.equals(f.get("disabled"))) {
                    continue;
                }

                filteredFields.add(f);
            }

            contentType.setFields(filteredFields);

            map.put((String) contentType.getSys().get("id"), contentType);
        }

        resourceList.contentTypes = map;
    }
}
