package com.contentful.discovery.loaders;

import com.contentful.discovery.ui.DisplayItem;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.lib.Constants;
import com.contentful.java.model.CDAContentType;
import com.contentful.java.model.CDAEntry;
import com.contentful.java.model.CDAResource;
import com.google.android.gms.maps.model.LatLng;

import org.markdownj.MarkdownProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Entry Preview Loader.
 * Prepares a {@code CDAEntry} to be displayed in preview mode.
 */
public class EntryPreviewLoader extends AbsAsyncTaskLoader<List<DisplayItem>> {
    private final CDAEntry entry;
    private final CDAContentType contentType;

    public EntryPreviewLoader(CDAEntry entry, CDAContentType contentType) {
        super();
        this.entry = entry;
        this.contentType = contentType;
    }

    @Override
    protected List<DisplayItem> performLoad() {
        List<DisplayItem> tmp = new ArrayList<DisplayItem>();
        List<Map> fields = contentType.getFields();
        MarkdownProcessor processor = new MarkdownProcessor();

        // Iterate Entry fields
        for (Map f : fields) {
            String id = (String) f.get("id");

            Object value = entry.getFields().get(id);

            // Skip if the value is empty
            if (value == null) {
                continue;
            }

            // Construct a new DisplayItem
            DisplayItem displayItem = new DisplayItem();

            // CDAFieldType of this item
            displayItem.fieldType = Constants.CDAFieldType.valueOf((String) f.get("type"));

            // Original field ID (from Content Type)
            displayItem.key = id;

            if (Constants.CDAFieldType.Text.equals(displayItem.fieldType)) {
                prepareTextItem(displayItem, value, processor);
            } else if (Constants.CDAFieldType.Link.equals(displayItem.fieldType)) {
                prepareLinkItem(displayItem, value);
            } else if (Constants.CDAFieldType.Location.equals(displayItem.fieldType)) {
                prepareLocationItem(displayItem, value);
            } else if (Constants.CDAFieldType.Array.equals(displayItem.fieldType)) {
                prepareArrayItem(displayItem, value, (Map) f.get("items"));
            } else {
                prepareDefaultItem(displayItem, value);
            }

            tmp.add(displayItem);
        }

        return tmp;
    }

    /**
     * Prepares item of generic type.
     */
    private void prepareDefaultItem(DisplayItem displayItem, Object value) {
        displayItem.displayValue = value.toString();
    }

    /**
     * Prepares item of type {@code Array}.
     */
    private void prepareArrayItem(DisplayItem displayItem, Object value, Map arrayItems) {
        displayItem.arrayItemType = Constants.CDAFieldType.valueOf((String) arrayItems.get("type"));

        if (Constants.CDAFieldType.Link.equals(displayItem.arrayItemType)) {
            displayItem.arrayLinkType = Constants.CDAResourceType.valueOf(
                    (String) arrayItems.get("linkType"));
        }

        displayItem.array = (List<Object>) value;
    }

    /**
     * Prepares item of type {@code Location}.
     */
    private void prepareLocationItem(DisplayItem displayItem, Object value) {
        Map map = (Map) value;
        displayItem.location = new LatLng((Double) map.get("lat"), (Double) map.get("lon"));

        displayItem.imageURI =
                Utils.getStaticMapUri(
                        displayItem.location.latitude,
                        displayItem.location.longitude);
    }

    /**
     * Prepares item of type {@code Link}.
     */
    private void prepareLinkItem(DisplayItem displayItem, Object value) {
        displayItem.resource = (CDAResource) value;

        if (value instanceof CDAEntry) {
            displayItem.displayValue = (String) ((CDAEntry) value).getFields()
                    .get(contentType.getDisplayField());
        }
    }

    /**
     * Prepares item of type {@code Text}.
     */
    private void prepareTextItem(DisplayItem displayItem, Object value, MarkdownProcessor processor) {
        displayItem.displayValue = TextProcessorLoader.wrapHtml(processor.markdown((String) value));
    }
}
