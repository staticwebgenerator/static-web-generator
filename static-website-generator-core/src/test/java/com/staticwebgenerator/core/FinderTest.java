package com.staticwebgenerator.core;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FinderTest {


    @Test
    void finderTest() throws IOException {
        Path path = Path.of("/Users/sdelamo/github/sdelamo/sergiodelamo.com/posts");
        Finder finder = new Finder("*.md");
        Files.walkFileTree(path, finder);
    }
}