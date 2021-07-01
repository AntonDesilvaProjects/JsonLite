package com.jsonlite.model;

import java.util.Iterator;
import java.util.Map;

public class ObjectNode extends JsonNode {
    public ObjectNode(Map<String, JsonNode> children) {
        super(null, children);
    }

    @Override
    public Type type() {
        return Type.OBJECT;
    }

    public ObjectNode put(String key, String value) {
        this.getChildren().put(key, new TextNode(value));
        return this;
    }

    public String toJsonString(boolean prettyPrint) {
        return toJsonString(prettyPrint ? 1 : -1);
    }

    @Override
    protected String toJsonString(int indent) {
        boolean isIndent = indent > 0;
        StringBuilder buffer = new StringBuilder("{");
        if (getChildren() != null) {
            Iterator<Map.Entry<String, JsonNode>> iterator = getChildren().entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, JsonNode> child = iterator.next();
                if (isIndent) {
                    buffer.append(generateIndentString(indent));
                }
                buffer.append("\"").append(child.getKey()).append("\"").append(": ").append(child.getValue().toJsonString(isIndent ? indent + 1 : -1));
                if (iterator.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
        if (isIndent) {
            buffer.append(generateIndentString(indent - 1));
        }
        buffer.append("}");
        return buffer.toString();
    }
}
