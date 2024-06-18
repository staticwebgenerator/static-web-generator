package com.staticwebgenerator.core;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.naming.Named;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@EachProperty("nav")
class Nav implements Named {
    private String name;

    public Nav(@Parameter String name) {
        this.name = name;
    }

    private List<Map<String, String>> items = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, String>> getItems() {
        return items;
    }

    public void setItems(List<Map<String, String>> items) {
        this.items = items;
    }
}

