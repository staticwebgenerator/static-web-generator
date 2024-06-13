package com.staticwebgenerator.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;

import java.io.File;

public interface WebsiteGenerator {
    void generateWebsite(
            @NonNull File postsDirectory,
            @NonNull File outputDirectory,
            @Nullable File globalMetadata);
}
