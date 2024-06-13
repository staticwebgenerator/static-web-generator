package com.staticwebgenerator.cli;

import com.staticwebgenerator.core.MetadataParser;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Map;

@Command(name = "swg", description = "Static Website Generator Command", mixinStandardHelpOptions = true)
public class StaticWebsiteGeneratorCliCommand implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(StaticWebsiteGeneratorCliCommand.class);

    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @Option(names = { "-m", "--metadata" }, description = "the global metadata file")
    File metadata;

    @Option(names = { "-p", "--posts" }, description = "the posts directory", required = true)
    File posts;

    @Option(names = { "-o", "--output" }, description = "the output directory", required = true)
    File output;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(StaticWebsiteGeneratorCliCommand.class, args);
    }

    public void run() {
        MetadataParser metadataParser = new MetadataParser();
        boolean globalMetadataSupplied = metadata != null && metadata.exists();
        if (!posts.isDirectory()) {
            info(true,posts.getAbsolutePath() + " is not a directory");
            return;
        }
        if (!output.exists()) {
            output.mkdir();
        }
        if (output.exists() && !output.isDirectory()) {
            info(true,output.getAbsolutePath() + " is not a directory");
            return;
        }
        try {
            Map<String, Object> globalMetadata = globalMetadataSupplied
                    ? metadataParser.parseProperties(metadata)
                    : Collections.emptyMap();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    void info(boolean verbose, String message) {
        if (verbose) {
            System.out.println(message);
        }
    }
}
