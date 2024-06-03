package com.staticwebgenerator.core;

import java.io.File;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public interface TestResourcesLoader {
    default File getFile(String path) {
        URL resource = getClass().getClassLoader().getResource(path);
        assertNotNull(resource);
        File file = new File(resource.getFile());
        assertTrue(file.exists());
        return file;
    }
}
