package com.staticwebgenerator.core;

import java.util.List;
import java.util.Map;

public record MarkdownDocument(Map<String, Object> metadata, List<String> lines) implements MetadataDocument {
}
