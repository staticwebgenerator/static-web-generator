package com.staticwebgenerator.core;

import jakarta.inject.Singleton;

@Singleton
class DefaultMarkdownDocumentRenderer implements MarkdownDocumentRenderer {
    private final MarkdownToHtml markdownToHtml;

    DefaultMarkdownDocumentRenderer(MarkdownToHtml markdownToHtml) {
        this.markdownToHtml = markdownToHtml;
    }

    @Override
    public HtmlDocument render(MarkdownDocument markdownDocument) {
        String markdown = String.join("\n", markdownDocument.lines());
        String html = markdownToHtml.render(markdown);
        return new HtmlDocument(markdownDocument.metadata(), markdownDocument.lines(), html);
    }
}
