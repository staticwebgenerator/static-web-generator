package com.staticwebgenerator.core;

import java.io.*;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class MetadataParser {
    public Map<String, Object> parse(Properties props) {
        return props.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));
    }

    public Map<String, Object> parse(File file) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return parse(props);
    }
}
