package com.staticwebgenerator.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import jakarta.inject.Singleton;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    public static final String MARKDOWN = "*.md";
    private final MetadataParser metadataParser;

    private final MetadataKeyReplacer metadataKeyReplacer;

    public DefaultWebsiteGenerator(MetadataParser metadataParser,
                                   MetadataKeyReplacer metadataKeyReplacer) {
        this.metadataParser = metadataParser;
        this.metadataKeyReplacer = metadataKeyReplacer;
    }

    public void generateWebsite(
            @NonNull File postsDirectory,
            @NonNull File outputDirectory,
            @Nullable File globalMetadataFile) {
        try {
            boolean globalMetadataSupplied = globalMetadataFile != null && globalMetadataFile.exists();
            Map<String, Object> globalMetadata = globalMetadataSupplied
                    ? metadataParser.parseProperties(globalMetadataFile)
                    : Collections.emptyMap();
            Path path = Path.of(postsDirectory.getAbsolutePath());
            Finder finder = new Finder(MARKDOWN);
            Files.walkFileTree(path, finder);
            finder.getPaths()
                    .parallelStream()
                    .map(p -> metadataParser.parseMarkdownDocument(p))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(md -> {
                        Map<String, Object> metadata = new MultipleMapAdapter(List.of(globalMetadata, md.metadata()));
                        return new MarkdownDocument(metadata, metadataKeyReplacer.replace(md.lines(), metadata));
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
