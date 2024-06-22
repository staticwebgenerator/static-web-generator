package com.staticwebgenerator.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class WebsiteGeneratorTest implements  TestResourcesLoader {


    @Test
    void generateWebsite(DefaultWebsiteGenerator websiteGenerator, NavParser navParser) throws IOException {
        File metadata = getFile("ajedrezcallejeroguadalajara/metadata.properties");

        File pagesDirectory = getFile("ajedrezcallejeroguadalajara/pages");
        assertTrue(pagesDirectory.isDirectory());

        File postsDirectory = getFile("ajedrezcallejeroguadalajara/posts");
        assertTrue(postsDirectory.isDirectory());

        File themeDirectory = getFile("ajedrezcallejeroguadalajara/theme");
        assertTrue(themeDirectory.isDirectory());

        File navFile = getFile("ajedrezcallejeroguadalajara/nav.properties");

        Collection<Nav> navs = navParser.parse(navFile);
        Map<String, Object> m = new HashMap<>();
        for (Nav nav : navs) {
            m.put(nav.getName() + "nav", nav);
        }
        websiteGenerator.render(themeDirectory, "/post.html", m);
    }
}