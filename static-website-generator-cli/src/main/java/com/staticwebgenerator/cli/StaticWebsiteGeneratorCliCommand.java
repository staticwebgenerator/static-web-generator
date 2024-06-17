package com.staticwebgenerator.cli;

import com.staticwebgenerator.core.WebsiteGenerator;
import io.micronaut.configuration.picocli.PicocliRunner;

import io.micronaut.context.BeanContext;
import io.micronaut.context.env.PropertySourceLoader;
import jakarta.inject.Inject;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;

@Command(name = "swg", description = "Static Website Generator Command", mixinStandardHelpOptions = true)
public class StaticWebsiteGeneratorCliCommand implements Runnable {
    @Option(names = {"-v", "--verbose"}, description = "...")
    boolean verbose;

    @Option(names = { "-m", "--metadata" }, description = "the global metadata file")
    File metadata;

    @Option(names = { "-n", "--nav" }, description = "the menu file", required = true)
    File nav;

    @Option(names = { "-p", "--posts" }, description = "the posts directory", required = true)
    File posts;

    @Option(names = { "-t", "--theme" }, description = "the theme directory", required = true)
    File theme;

    @Option(names = { "-o", "--output" }, description = "the output directory", required = true)
    File output;

    @Inject
    WebsiteGenerator websiteGenerator;

    @Inject
    BeanContext beanContext;

    @Inject
    PropertySourceLoader propertySourceLoader;

    public static void main(String[] args) throws Exception {
        PicocliRunner.run(StaticWebsiteGeneratorCliCommand.class, args);

    }

    public void run() {
        if (!posts.isDirectory()) {
            System.out.println(posts.getAbsolutePath() + " is not a directory");
            return;
        }
        if (!theme.isDirectory()) {
            System.out.println(theme.getAbsolutePath() + " is not a directory");
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
