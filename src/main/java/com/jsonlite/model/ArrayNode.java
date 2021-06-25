package com.jsonlite.model;

import java.util.Map;

public class ArrayNode extends JsonNode {
    public ArrayNode(Map<String, JsonNode> children) {
        super(null, children);
    }

    @Override
    public Type type() {
        return Type.ARRAY;
    }
}
