package com.staticwebgenerator.core;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MetadataKeyReplacerTest {
    @Test
    void testReplace() {
        MetadataKeyReplacer metadataKeyReplacer = new DefaultMetadataKeyReplacer();
        String text = "[%me]";
        String replaced = metadataKeyReplacer.replace(text, Map.of("Customer", "M. Bluth",
                "Me", "Bob Loblaw",
                "Date", "April 3rd, 2023"));
        assertEquals("Bob Loblaw", replaced);

        List<String> result = metadataKeyReplacer.replace(List.of("Sincerely,","[%me]"), Map.of("Customer", "M. Bluth",
                "Me", "Bob Loblaw",
                "Date", "April 3rd, 2023"));
        assertEquals(List.of("Sincerely,","Bob Loblaw"), result);
    }
}