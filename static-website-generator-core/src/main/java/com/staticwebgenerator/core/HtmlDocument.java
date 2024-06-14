package com.staticwebgenerator.core;
import java.util.Map;

public record HtmlDocument(Map<String, Object> metadata, String html) implements MetadataDocument {
}
