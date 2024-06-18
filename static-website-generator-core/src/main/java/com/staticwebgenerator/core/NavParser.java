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
        defaultEnvironment.addPropertySource("menu", propertiesToMap(file));
        return applicationContext.getBeansOfType(Nav.class);
    }

    private static Map<String, Object> propertiesToMap(File file) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file)) {
            props.load(inputStream);
        }
        return propertiesToMap(props);
    }

    private static Map<String, Object> propertiesToMap(Properties props) {
        Map<String, Object> m = new HashMap<>();
        for (Map.Entry<Object, Object> entry : props.entrySet()) {
            m.put(entry.getKey().toString(), entry.getValue());
        }
        return m;
    }
}
