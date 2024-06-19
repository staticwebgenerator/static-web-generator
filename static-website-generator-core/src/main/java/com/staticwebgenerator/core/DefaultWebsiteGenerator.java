package com.staticwebgenerator.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.views.ViewUtils;
import io.micronaut.views.ViewsConfiguration;
import io.micronaut.views.thymeleaf.ThymeleafViewsRendererConfiguration;
import jakarta.inject.Singleton;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.EngineContext;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    public static final String MARKDOWN = "*.md";
    private final MetadataParser metadataParser;
    private final MetadataKeyReplacer metadataKeyReplacer;
    private final ThymeleafViewsRendererConfiguration thymeleafViewsRendererConfiguration;

    public DefaultWebsiteGenerator(ThymeleafViewsRendererConfiguration thymeleafViewsRendererConfiguration,
                                   MetadataParser metadataParser,
                                   MetadataKeyReplacer metadataKeyReplacer, ThymeleafViewsRendererConfiguration thymeleafViewsRendererConfiguration1) {
        this.metadataParser = metadataParser;
        this.metadataKeyReplacer = metadataKeyReplacer;
        this.thymeleafViewsRendererConfiguration = thymeleafViewsRendererConfiguration1;
    }

    public void generateWebsite(
            @NonNull File navFile,
            @NonNull File themeDirectory,
            @NonNull File postsDirectory,
            @NonNull File outputDirectory,
            @Nullable File globalMetadataFile) {
        try {
            boolean globalMetadataSupplied = globalMetadataFile != null && globalMetadataFile.exists();
            Map<String, Object> globalMetadata = globalMetadataSupplied
                    ? metadataParser.parseProperties(globalMetadataFile)
                    : Collections.emptyMap();
            Path path = Path.of(postsDirectory.getAbsolutePath());
            Finder finder = new Finder(MARKDOWN);
            Files.walkFileTree(path, finder);
            finder.getPaths()
                    .parallelStream()
                    .map(p -> metadataParser.parseMarkdownDocument(p))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .map(md -> {
                        Map<String, Object> metadata = new MultipleMapAdapter(List.of(globalMetadata, md.metadata()));
                        return new MarkdownDocument(metadata, metadataKeyReplacer.replace(md.lines(), metadata));
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void render(File themeDirectory, String templateName, Map<String, Object> model) {
        AbstractConfigurableTemplateResolver abstractConfigurableTemplateResolver = initializeTemplateResolver(themeDirectory, thymeleafViewsRendererConfiguration);
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(abstractConfigurableTemplateResolver);

        TemplateEngine templateEngine = new TemplateEngine();
        FileTemplateResolver templateResolver = new FileTemplateResolver ();
        templateResolver.setPrefix(themeDirectory.getAbsolutePath());
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setOrder(templateEngine.getTemplateResolvers().size());
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setCacheable(false);
        templateResolver.setCheckExistence(true);

        templateEngine.setTemplateResolver(templateResolver);

        IContext context = new Context(Locale.getDefault(), Collections.emptyMap());

        Writer writer = new StringWriter();
        templateEngine.process(templateName, Collections.emptySet(), context, writer);
        String sb = writer.toString();
        String foo = "";
    }



    private AbstractConfigurableTemplateResolver initializeTemplateResolver(File themeDirectory,
                                                                            ThymeleafViewsRendererConfiguration thConfiguration) {
        FileTemplateResolver templateResolver = new FileTemplateResolver();

        templateResolver.setPrefix(ViewUtils.normalizeFolder(themeDirectory.getAbsolutePath()));
        templateResolver.setCharacterEncoding(thConfiguration.getCharacterEncoding());
        templateResolver.setTemplateMode(thConfiguration.getTemplateMode());
        templateResolver.setSuffix(thConfiguration.getSuffix());
        templateResolver.setForceSuffix(thConfiguration.getForceSuffix());
        templateResolver.setForceTemplateMode(thConfiguration.getForceTemplateMode());
        templateResolver.setCacheTTLMs(thConfiguration.getCacheTTLMs());
        templateResolver.setCheckExistence(thConfiguration.getCheckExistence());
        templateResolver.setCacheable(thConfiguration.getCacheable());
        return templateResolver;
    }

}
