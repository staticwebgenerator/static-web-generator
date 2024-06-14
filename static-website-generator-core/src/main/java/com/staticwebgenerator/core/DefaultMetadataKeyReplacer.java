package com.staticwebgenerator.core;

import jakarta.inject.Singleton;

import java.util.Map;

@Singleton
class DefaultMetadataKeyReplacer implements MetadataKeyReplacer {

    private static final String PREFIX = "[%";
    private static final String SUFFIX = "]";

    @Override
    public String replace(String text, Map<String, Object> metadata) {
        if (!containsMetadataKey(text)) {
            return text;
        }
        String result = text;
        for (String k : metadata.keySet()) {
            String value = metadata.get(k).toString();
            result = result.replaceAll(metadataKey(k), value);
            result = result.replaceAll(metadataKey(k.toLowerCase()), value);
            result = result.replaceAll(metadataKey(k.toUpperCase()), value);
        }
        return result;
    }

    private static String metadataKey(String key) {
        return "\\[\\%" + key + "\\]";
    }

    private static boolean containsMetadataKey(String text) {
        return text.contains(PREFIX) && text.contains(SUFFIX);
    }
}
