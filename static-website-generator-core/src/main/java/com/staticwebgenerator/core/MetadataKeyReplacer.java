package com.staticwebgenerator.core;

import java.util.List;
import java.util.Map;

@FunctionalInterface
public interface MetadataKeyReplacer {
    String replace(String text, Map<String, Object> metadata);

    default List<String> replace(List<String> lines, Map<String, Object> metadata) {
        return lines.stream()
                .map(line -> replace(line, metadata))
                .toList();
    }
}
