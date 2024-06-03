package com.staticwebgenerator.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MultipleMapAdapterTest implements TestResourcesLoader {

    @Test
    void multipleMapAdapter() throws IOException {
        File propertiesFile = getFile("liftoff/metadata.properties");
        MetadataParser parser = new MetadataParser();
        Map<String, Object> globalMetadata = parser.parseProperties(propertiesFile);
        File file = getFile("liftoff/news-starcity.md");
        Map<String, Object> fileMetadata = parser.parseMarkdownDocument(file).metadata();
        Map<String, Object> metadata = new MultipleMapAdapter(List.of(fileMetadata, globalMetadata));
        assertEquals(Map.of("title", "Star City",
                "date","2003-06-03",
                "description","Liftoff to Space Exploration."), metadata);
    }
}