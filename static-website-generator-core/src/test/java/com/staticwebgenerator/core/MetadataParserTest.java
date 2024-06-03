package com.staticwebgenerator.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MetadataParserTest implements TestResourcesLoader {

    @Test
    void parsePropertiesFile() throws IOException {
        File file = getFile("sergiodelamoweb/metadata.properties");
        MetadataParser metadataParser = new MetadataParser();
        Map<String, Object> metadata = metadataParser.parseProperties(file);
        assertEquals(Map.of("creator", "Sergio del Amo",
                "author","Sergio del Amo",
                "generator","Static Web Generator",
                "keywords","gradle,spock,geb,grails,groovy','java,wordpress,woocommerce,micronaut",
                "robots","index,follow",
                "googlebot","all"), metadata);
    }

    @Test
    void parseMarkdownFile() throws IOException {
        File file = getFile("liftoff/news-starcity.md");

        MetadataParser metadataParser = new MetadataParser();
        Map<String, Object> metadata = metadataParser.parseMarkdownDocument(file).metadata();
        assertEquals(Map.of("title", "Star City",
                "date","2003-06-03"), metadata);
    }
}