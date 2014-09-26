package com.contentful.discovery.loaders;

import com.contentful.discovery.utils.Utils;

import org.markdownj.MarkdownProcessor;

/**
 * Text Processor Loader.
 * Processes plain text, generating HTML for Markdown.
 */
public class TextProcessorLoader extends AbsAsyncTaskLoader<String> {
    private final String text;

    public TextProcessorLoader(String text) {
        super();
        this.text = text;
    }

    @Override
    protected String performLoad() {
        return wrapHtml(new MarkdownProcessor().markdown(text));
    }

    public static String wrapHtml(String html) {
        return new StringBuilder()
                .append("<html><head>")
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
