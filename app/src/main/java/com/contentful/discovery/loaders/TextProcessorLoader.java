package com.contentful.discovery.loaders;

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
                .append("<link href='http://fonts.googleapis.com/css?family=Lato&subset=latin,latin-ext' rel='stylesheet' type='text/css' />")
                .append("<style type=\"text/css\">")
                .append("@font-face {")
                .append("font-family: \"lato\";")
                .append("src: url('file:///android_asset/fonts/lato_regular.ttf');")
                .append("}")
                .append("body {font-family: \"lato\";}")
                .append("</style>")
                .append("</head><body>")
                .append(html)
                .append("</body></html>")
                .toString();
    }
}
