package com.staticwebgenerator.core;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import static org.junit.jupiter.api.Assertions.*;

class MetadataParserTest {

    @Test
    void parsePropertiesFile() throws IOException {
        URL resource = getClass().getClassLoader().getResource("sergiodelamoweb/metadata.properties");
        assertNotNull(resource);
        File file = new File(resource.getFile());
        assertTrue(file.exists());

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
        URL resource = getClass().getClassLoader().getResource("liftoff/news-starcity.md");
        assertNotNull(resource);
        File file = new File(resource.getFile());
        assertTrue(file.exists());

        MetadataParser metadataParser = new MetadataParser();
        Map<String, Object> metadata = metadataParser.parseMarkdown(file);
        assertEquals(Map.of("title", "Star City",
                "date","2003-06-03"), metadata);
    }
}