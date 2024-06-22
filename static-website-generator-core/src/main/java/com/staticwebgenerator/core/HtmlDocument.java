package com.staticwebgenerator.core;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public record HtmlDocument(Map<String, Object> metadata, List<String> lines, String html) implements MetadataDocument {

    public static final String META_DESCRIPTION = "description";

    public Optional<String> getDescription() {
        return getMetadataValue(META_DESCRIPTION);
    }


    public Optional<String> getMetadataValue(String key) {
        return Optional.ofNullable(metadata.get(key))
            .map(Object::toString);
    }
}
