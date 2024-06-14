package com.staticwebgenerator.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class MarkdownDocumentRendererTest {

    @Test
    void testRender(MarkdownDocumentRenderer renderer) {
        MarkdownDocument markdownDocument = new MarkdownDocument(Collections.emptyMap(), List.of("No. **I don't want to do it**"));
        HtmlDocument htmlDocument = renderer.render(markdownDocument);
        assertEquals("<p>No. <strong>I don't want to do it</strong></p>\n", htmlDocument.html());
    }

}