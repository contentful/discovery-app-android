package com.contentful.discovery.loaders;

import org.markdownj.MarkdownProcessor;

/**
 * Text Processor Loader.
 * Processes plain text, generating HTML for Markdown.
 */
public class TextProcessorLoader extends AbsAsyncTaskLoader<String> {
    private String text;

    public TextProcessorLoader(String text) {
        super();
        this.text = text;
    }

    @Override
    protected String performLoad() {
        return new MarkdownProcessor().markdown(text);
    }
}
