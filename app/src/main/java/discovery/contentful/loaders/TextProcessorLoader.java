package discovery.contentful.loaders;

import discovery.contentful.utils.Utils;
import org.markdownj.MarkdownProcessor;

public class TextProcessorLoader extends AbsAsyncTaskLoader<String> {
  private final String text;

  public TextProcessorLoader(String text) {
    super();
    this.text = text;
  }

  @Override protected String performLoad() {
    return wrapHtml(new MarkdownProcessor().markdown(text));
  }

  public static String wrapHtml(String html) {
    return new StringBuilder().append("<html><head>")
        .append("<style type=\"text/css\">")
        .append("@font-face {font-family: \"lato\"; src: url('file:///android_asset/")
        .append(Utils.FONT_LATO_REGULAR)
        .append("'); }")
        .append("body {font-family: \"lato\";}")
        .append("</style></head><body>")
        .append(html)
        .append("</body></html>")
        .toString();
  }
}
