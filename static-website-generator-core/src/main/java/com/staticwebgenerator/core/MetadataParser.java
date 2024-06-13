package com.staticwebgenerator.core;

import jakarta.inject.Singleton;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.FileVisitResult.CONTINUE;
import static java.nio.file.FileVisitResult.TERMINATE;

@Singleton
public class MetadataParser {
    private static final Logger LOG = LoggerFactory.getLogger(MetadataParser.class);
    private static final String METADATA_DELIMETER = "---";
    public static final String SUFFIX_MD = ".md";
    public static final String SUFFIX_MARKDOWN = ".markdown";

    public Map<String, Object> parseProperties(Properties props) {
        return props.entrySet().stream()
                .collect(Collectors.toMap(
                        e -> e.getKey().toString(),
                        Map.Entry::getValue
                ));
    }


    public List<Path> markdownPaths(Path path) throws IOException {
        List<Path> paths = new ArrayList<>();
        Files.walkFileTree(path, new FileVisitor<>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) {
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                String fileName = file.getFileName().toString();
                if (fileName.endsWith(SUFFIX_MD) || fileName.endsWith(SUFFIX_MARKDOWN)) {
                    paths.add(file);
                }
                return CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) {
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                return FileVisitResult.CONTINUE;
            }
        });
        return paths;
    }

    public Optional<MarkdownDocument> parseMarkdownDocument(Path path) {
        try {
        return parseMarkdownDocument(new FileReader(path.toFile()));
        } catch (IOException e) {
            LOG.error("{}", e);
            return Optional.empty();
        }
    }

    public Optional<MarkdownDocument> parseMarkdownDocument(File file)  {
        try {
            return parseMarkdownDocument(new FileReader(file));
        } catch (IOException e) {
            LOG.error("{}", e);
            return Optional.empty();
        }
    }

    public Optional<MarkdownDocument> parseMarkdownDocument(FileReader fileReader) {
        try {
            Map<String, Object> metadata = new HashMap<>();
            List<String> lines = new ArrayList<>();
            try (BufferedReader br = new BufferedReader(fileReader)) {
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
                    if (!readingMetadata) {
                        lines.add(line);
                    }
                }
            }
            return Optional.of(new MarkdownDocument(metadata, lines));

        } catch (IOException e) {
            LOG.error("{}", e);
            return Optional.empty();
        }
    }

    public Map<String, Object> parseProperties(File file) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return parseProperties(props);
    }
}
