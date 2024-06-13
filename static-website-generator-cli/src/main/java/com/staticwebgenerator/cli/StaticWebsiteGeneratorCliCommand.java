package com.staticwebgenerator.cli;

import com.staticwebgenerator.core.Finder;
import com.staticwebgenerator.core.MetadataParser;
import com.staticwebgenerator.core.WebsiteGenerator;
import io.micronaut.configuration.picocli.PicocliRunner;
import io.micronaut.context.ApplicationContext;

import jakarta.inject.Inject;
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

    @Inject
    WebsiteGenerator websiteGenerator;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(StaticWebsiteGeneratorCliCommand.class, args);
    }

    public void run() {
        if (!posts.isDirectory()) {
            System.out.println(posts.getAbsolutePath() + " is not a directory");
            return;
        }
        if (!output.exists()) {
            output.mkdir();
        }
        if (output.exists() && !output.isDirectory()) {
            System.out.println(output.getAbsolutePath() + " is not a directory");
            return;
        }

        websiteGenerator.generateWebsite(posts, output, metadata);
    }
}
