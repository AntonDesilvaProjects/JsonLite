package com.jsonlite.model;

import java.util.Map;
import java.util.Set;
/*
 *  Scalar Nodes:
 *   a) TextNode
 *   b) BooleanNode
 *   c) NumberNode
 *   d) NullNode
 *
 *  Complex Nodes:
 *   a) ObjectNode
 *   b) ArrayNode
 * */
public abstract class JsonNode {
    enum Type {
        TEXT, BOOLEAN, NUMBER, NULL, OBJECT, ARRAY
    }

    private String key;
    private Object value; // for scalar values
    private Map<String, JsonNode> children; // for complex values

    public JsonNode(Object value, Map<String, JsonNode> children) {
        this.value = value;
        this.children = children;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public Map<String, JsonNode> getChildren() {
        return children;
    }

    public void setChildren(Map<String, JsonNode> children) {
        this.children = children;
    }

    public abstract Type type();
}
