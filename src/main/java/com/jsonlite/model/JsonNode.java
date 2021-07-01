package com.jsonlite.model;

import java.util.Map;
import java.util.Optional;
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
 *
 *  Functionalities:
 *  a) printing
 *  b) pretty-printing
 *  c) quering
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

    public abstract String toJsonString(boolean prettyPrint);

    protected abstract String toJsonString(int indent);

    protected String generateIndentString(int indent) {
        StringBuilder indentString = new StringBuilder("\n");
        if (indent > 0) {
            indentString.append("   ".repeat(indent));
        }
        return indentString.toString();
    }

    public Optional<JsonNode> resolve(String jsonPtr) {
        if (jsonPtr == null || !jsonPtr.startsWith("/")) {
            throw new IllegalArgumentException("A JSON pointer must be non-null with '/' as the first character");
        }
        final String[] path = jsonPtr.split("/");
        JsonNode node = this;
        for (int i = 1; i < path.length; i++) {
            String childPath = path[i];
            // handle the escaping of ~ = ~0, / = ~1
            childPath = childPath.replaceAll("~0", "~")
                    .replaceAll("~1", "/");
            if (node.children != null && node.children.containsKey(childPath)) {
                node = node.children.get(childPath);
            } else {
                return Optional.empty();
            }
        }
        return Optional.of(node);
    }
}
