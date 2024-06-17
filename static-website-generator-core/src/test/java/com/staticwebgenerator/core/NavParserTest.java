package com.staticwebgenerator.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class NavParserTest implements TestResourcesLoader {

    @Inject
    NavParser navParser;

    @Test
    void parse() throws IOException {
        File navFile = getFile("nav.properties");
        Collection<Nav> navs = navParser.parse(navFile);
        assertNotNull(navs);
        assertTrue( navs.stream().filter(nav -> nav.getName().equals("main")).findFirst().isPresent());
    }
}