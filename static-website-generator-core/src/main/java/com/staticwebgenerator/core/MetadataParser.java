package com.staticwebgenerator.core;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public class MetadataParser {
    private static String METADATA_DELIMETER = "---";

    public Map<String, Object> parseProperties(Properties props) {
        return props.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));
    }

    public Map<String, Object> parseMarkdown(File file) throws IOException {
        Map<String, Object> metadata = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            boolean readingMetadata = false;
            String line;
            while ((line = br.readLine()) != null) {
                if (line.equals(METADATA_DELIMETER)) {
                    readingMetadata = !readingMetadata;
                    continue;
                }
                if (readingMetadata) {
                    String[] arr = line.split(":");
                    if (arr.length == 2) {
                        metadata.put(arr[0].trim(), arr[1].trim());
                    }
                }
                // process the line.
            }
        }
        return metadata;
    }

    public Map<String, Object> parseProperties(File file) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return parseProperties(props);
    }
}
