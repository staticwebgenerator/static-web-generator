package com.staticwebgenerator.core;

@FunctionalInterface
public interface MarkdownDocumentRenderer {
    HtmlDocument render(MarkdownDocument markdownDocument);
}
