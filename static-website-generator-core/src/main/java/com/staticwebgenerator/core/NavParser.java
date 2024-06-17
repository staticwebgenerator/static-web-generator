package com.staticwebgenerator.core;

import io.micronaut.context.ApplicationContext;
import io.micronaut.context.BeanContext;
import io.micronaut.context.env.*;
import io.micronaut.runtime.context.scope.refresh.RefreshEvent;
import jakarta.inject.Singleton;

import java.awt.*;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Singleton
public class NavParser {

    private final DefaultEnvironment defaultEnvironment;
    private final ApplicationContext applicationContext;

    public NavParser(DefaultEnvironment defaultEnvironment, ApplicationContext applicationContext) {
        this.defaultEnvironment = defaultEnvironment;
        this.applicationContext = applicationContext;
    }

    public Collection<Nav> parse(File file) throws IOException {

        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        Map<String, Object> finalMap = new HashMap<>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            finalMap.put(entry.getKey().toString(), entry.getValue());
        }
        MapPropertySource mapPropertySource = new MapPropertySource("menu", finalMap);


        defaultEnvironment.addPropertySource(mapPropertySource);
        applicationContext.publishEvent(new RefreshEvent());

        return applicationContext.getBeansOfType(Nav.class);
    }
}
