package com.staticwebgenerator.core;

import io.micronaut.test.extensions.junit5.annotation.MicronautTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@MicronautTest(startApplication = false)
class NavParserTest implements TestResourcesLoader {

    @Inject
    NavParser navParser;

    @Test
    void parse() throws IOException {
        File navFile = getFile("micronaut/nav.properties");
        Collection<Nav> navs = navParser.parse(navFile);
        assertNotNull(navs);
        Optional<Nav> mainOptional = navs.stream()
                .filter(nav -> nav.getName().equals("main"))
                .findFirst();
        assertTrue(mainOptional.isPresent());
        Nav main = mainOptional.get();
        assertNotNull(main);
        assertFalse(main.getItems().isEmpty());

        Optional<Nav> secondaryOptional = navs.stream()
                .filter(nav -> nav.getName().equals("secondary"))
                .findFirst();
        assertTrue(secondaryOptional.isPresent());
        Nav secondary = secondaryOptional.get();
        assertNotNull(secondary);

        assertFalse(secondary.getItems().isEmpty());
        assertTrue(secondary.getItems()
                .stream()
                .map(m -> new NavItem(m.get("label"), m.get("href")))
                .anyMatch(item -> new NavItem("Brand Guidelines", "/brand-guidelines/").equals(item)));
    }
}