package com.jsonlite.model;

import java.util.Map;

public class ObjectNode extends JsonNode {
    public ObjectNode(Map<String, JsonNode> children) {
        super(null, children);
    }

    @Override
    public Type type() {
        return Type.OBJECT;
    }
}
