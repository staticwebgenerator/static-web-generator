package com.staticwebgenerator.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.assertTrue;

@MicronautTest(startApplication = false)
class WebsiteGeneratorTest implements  TestResourcesLoader {


    @Test
    void generateWebsite(DefaultWebsiteGenerator websiteGenerator) {
        File themeDirectory = getFile("ajedrezcallejeroguadalajara/theme");
        assertTrue(themeDirectory.isDirectory());

        websiteGenerator.render(themeDirectory, "/post.html");
    }
}