package com.staticwebgenerator.core;

import io.micronaut.core.annotation.NonNull;

@FunctionalInterface
public interface MarkdownToHtml {
    @NonNull
    String render(@NonNull String markdown);
}
