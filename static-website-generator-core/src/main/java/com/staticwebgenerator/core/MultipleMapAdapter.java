package com.staticwebgenerator.core;

import java.util.*;

public class MultipleMapAdapter implements Map<String, Object> {
    private final Map<String, Object> m;
    public MultipleMapAdapter(List<Map<String, Object>> maps) {
        m = new HashMap<>();
        for (Map<String, Object> map : maps) {
            for (String k : map.keySet()) {
                if (!m.containsKey(k)) {
                    m.put(k, map.get(k));
                }
            }
        }
    }

    @Override
    public int size() {
        return m.size();
    }

    @Override
    public boolean isEmpty() {
        return m.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return m.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return m.containsValue(value);
    }

    @Override
    public Object get(Object key) {
        return m.get(key);
    }

    @Override
    public Object put(String key, Object value) {
        return m.put(key, value);
    }

    @Override
    public Object remove(Object key) {
        return m.remove(key);
    }

    @Override
    public void putAll(Map<? extends String, ?> m) {
        this.m.putAll(m);
    }

    @Override
    public void clear() {
        m.clear();
    }

    @Override
    public Set<String> keySet() {
        return m.keySet();
    }

    @Override
    public Collection<Object> values() {
        return m.values();
    }

    @Override
    public Set<Entry<String, Object>> entrySet() {
        return m.entrySet();
    }
}
