package com.jsonlite.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class ArrayNode extends JsonNode {
    public ArrayNode(Map<String, JsonNode> children) {
        super(null, children);
    }

    @Override
    public Type type() {
        return Type.ARRAY;
    }

    @Override
    public String toJsonString(boolean prettyPrint) {
        return toJsonString(prettyPrint ? 1 : -1);
    }

    @Override
    protected String toJsonString(int indent) {
        boolean isIndent = indent > 0;
        StringBuilder buffer = new StringBuilder("[");
        if (getChildren() != null) {
            Iterator<Map.Entry<String, JsonNode>> iterator = getChildren().entrySet().iterator();
            while (iterator.hasNext()) {
                if (isIndent) {
                    buffer.append(generateIndentString(indent));
                }
                buffer.append(iterator.next().getValue().toJsonString(isIndent ? indent + 1 : -1));
                if (iterator.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
        if (isIndent) {
            buffer.append(generateIndentString(indent - 1));
        }
        buffer.append("]");
        return buffer.toString();
    }

    public List<JsonNode> query(Predicate<JsonNode> filter) {
        final List<JsonNode> matches = new ArrayList<>();
        final Map<String, JsonNode> children = getChildren();
        if (children != null && !children.isEmpty()) {
            for (int i = 0; i < children.size(); i++) {
                JsonNode currentNode = getChildren().get(String.valueOf(i));
                if (filter.test(currentNode)) {
                    matches.add(currentNode);
                }
            }
        }
        return matches;
    }
}
