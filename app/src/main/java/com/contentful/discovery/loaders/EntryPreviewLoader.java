package com.contentful.discovery.loaders;

import com.contentful.discovery.ui.DisplayItem;
import com.contentful.discovery.utils.Utils;
import com.contentful.java.cda.CDAContentType;
import com.contentful.java.cda.CDAEntry;
import com.contentful.java.cda.CDAField;
import com.contentful.java.cda.CDAResource;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.markdownj.MarkdownProcessor;

public class EntryPreviewLoader extends AbsAsyncTaskLoader<List<DisplayItem>> {
  private final CDAEntry entry;
  private final CDAContentType contentType;

  public EntryPreviewLoader(CDAEntry entry, CDAContentType contentType) {
    super();
    this.entry = entry;
    this.contentType = contentType;
  }

  @Override protected List<DisplayItem> performLoad() {
    List<DisplayItem> tmp = new ArrayList<>();
    List<CDAField> fields = contentType.fields();
    MarkdownProcessor processor = new MarkdownProcessor();

    // Iterate Entry fields
    for (CDAField f : fields) {
      String id = f.id();

      Object value = entry.getField(id);

      // Skip if the value is empty
      if (value == null) {
        continue;
      }

      // Construct a new DisplayItem
      DisplayItem displayItem = new DisplayItem();

      // CDAFieldType of this item
      displayItem.fieldType = f.type();

      // Original field ID (from Content Type)
      displayItem.key = id;

      if ("Text".equals(displayItem.fieldType)) {
        prepareTextItem(displayItem, value, processor);
      } else if ("Link".equals(displayItem.fieldType)) {
        prepareLinkItem(displayItem, value);
      } else if ("Location".equals(displayItem.fieldType)) {
        prepareLocationItem(displayItem, value);
      } else if ("Array".equals(displayItem.fieldType)) {
        prepareArrayItem(displayItem, value, f.items());
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
  @SuppressWarnings("unchecked")
  private void prepareArrayItem(DisplayItem displayItem, Object value, Map arrayItems) {
    displayItem.arrayItemType = (String) arrayItems.get("type");

    if ("Link".equals(displayItem.arrayItemType)) {
      displayItem.arrayLinkType = (String) arrayItems.get("linkType");
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
        Utils.getStaticMapUri(displayItem.location.latitude, displayItem.location.longitude);
  }

  /**
   * Prepares item of type {@code Link}.
   */
  private void prepareLinkItem(DisplayItem displayItem, Object value) {
    displayItem.resource = (CDAResource) value;

    if (value instanceof CDAEntry) {
      displayItem.displayValue = Utils.getTitleForEntry((CDAEntry) value, contentType);
    }
  }

  /**
   * Prepares item of type {@code Text}.
   */
  private void prepareTextItem(DisplayItem displayItem, Object value, MarkdownProcessor processor) {
    displayItem.displayValue = TextProcessorLoader.wrapHtml(processor.markdown((String) value));
  }
}
