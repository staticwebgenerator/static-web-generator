package com.staticwebgenerator.core;

import io.micronaut.core.annotation.NonNull;
import io.micronaut.core.annotation.Nullable;
import io.micronaut.views.ViewUtils;
import io.micronaut.views.thymeleaf.ThymeleafViewsRendererConfiguration;
import jakarta.inject.Singleton;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.context.IContext;
import org.thymeleaf.templateresolver.AbstractConfigurableTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

@Singleton
public class DefaultWebsiteGenerator implements WebsiteGenerator {
    public static final String MARKDOWN = "*.md";
    public static final String HTML = "*.html";
    public static final String TEMPLATE_POST = "post.html";
    public static final String MODEL_METAS = "metas";
    public static final String MODEL_HTML = "html";
    private final MetadataParser metadataParser;
    private final MetadataKeyReplacer metadataKeyReplacer;
    private final ThymeleafViewsRendererConfiguration thymeleafViewsRendererConfiguration;
    private final MarkdownDocumentRenderer markdownDocumentRenderer;
    private final NavParser navParser;

    public DefaultWebsiteGenerator(ThymeleafViewsRendererConfiguration thymeleafViewsRendererConfiguration,
                                   MetadataParser metadataParser,
                                   MetadataKeyReplacer metadataKeyReplacer,
                                   MarkdownDocumentRenderer markdownDocumentRenderer,
                                   NavParser navParser) {
        this.metadataParser = metadataParser;
        this.metadataKeyReplacer = metadataKeyReplacer;
        this.thymeleafViewsRendererConfiguration = thymeleafViewsRendererConfiguration;
        this.markdownDocumentRenderer = markdownDocumentRenderer;
        this.navParser = navParser;
    }

    public void generateWebsite(
            @NonNull File navFile,
            @NonNull File themeDirectory,
            @NonNull File pagesDirectory,
            @NonNull File postsDirectory,
            @NonNull File outputDirectory,
            @Nullable File globalMetadataFile) {
        try {
            Collection<Nav> navs = navParser.parse(navFile);

            boolean globalMetadataSupplied = globalMetadataFile != null && globalMetadataFile.exists();
            Map<String, Object> globalMetadata = globalMetadataSupplied
                    ? metadataParser.parseProperties(globalMetadataFile)
                    : Collections.emptyMap();
            Path path = Path.of(postsDirectory.getAbsolutePath());

            metadataDocumentStream(path, HTML)
                    .map(md -> {
                        Map<String, Object> metadata = new MultipleMapAdapter(List.of(globalMetadata, md.metadata()));
                        List<String> lines = metadataKeyReplacer.replace(md.lines(), metadata);
                        return new HtmlDocument(metadata, lines, String.join("\n", lines));
                    }).forEach(htmlDocument -> {

                            });

            metadataDocumentStream(path, MARKDOWN)
                    .map(md -> {
                        Map<String, Object> metadata = new MultipleMapAdapter(List.of(globalMetadata, md.metadata()));
                        return new MarkdownDocument(metadata, metadataKeyReplacer.replace(md.lines(), metadata));
                    })
                    .map(md -> markdownDocumentRenderer.render(md))
                    .forEach(htmlDocument -> {
                        List<Meta> metas = metas(htmlDocument);
                        render(themeDirectory, TEMPLATE_POST, Map.of(MODEL_METAS, metas, MODEL_HTML, htmlDocument.html()));
                    });

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Stream<MetadataDocument> metadataDocumentStream(Path path, String pattern) throws IOException {
        Finder finder = new Finder(pattern);
        Files.walkFileTree(path, finder);
        return finder.getPaths()
                .parallelStream()
                .map(metadataParser::parseMetadataDocument)
                .filter(Optional::isPresent)
                .map(Optional::get);
    }

    private static List<Meta> metas(HtmlDocument htmlDocument) {
        List<Meta> metas = new ArrayList<>();
        htmlDocument.getDescription()
                .ifPresent(description -> metas.add(new Meta("description", description)));
        return metas;
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
