package com.staticwebgenerator.core;

import io.micronaut.context.annotation.EachProperty;
import io.micronaut.context.annotation.Parameter;
import io.micronaut.core.naming.Named;
import io.micronaut.runtime.context.scope.Refreshable;

import java.util.ArrayList;
import java.util.List;

@Refreshable
@EachProperty("menus")
public class Nav implements Named {
    private String name;

    public Nav(@Parameter String name) {
        this.name = name;
    }

    private List<NavItem> items = new ArrayList<>();

    @Override
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<NavItem> getItems() {
        return items;
    }

    public void setItems(List<NavItem> items) {
        this.items = items;
    }
}
