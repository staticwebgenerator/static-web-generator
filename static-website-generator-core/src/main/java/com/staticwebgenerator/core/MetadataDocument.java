package com.staticwebgenerator.core;

import java.util.List;
import java.util.Map;

public interface MetadataDocument {
    Map<String, Object> metadata();
     List<String> lines();
}
