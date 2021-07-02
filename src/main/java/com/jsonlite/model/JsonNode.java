package com.jsonlite.model;

import org.w3c.dom.Text;

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
        TEXT(TextNode.class),
        BOOLEAN(BooleanNode.class),
        NUMBER(NumberNode.class),
        NULL(NullNode.class),
        OBJECT(ObjectNode.class),
        ARRAY(ArrayNode.class);

        private Class<?> clazz;

        Type(Class<?> clazz) {
            this.clazz = clazz;
        }

        public Class<?> getClazz() {
            return clazz;
        }
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

    public boolean hasChild(String nameOrIdx) {
        return children != null && children.containsKey(nameOrIdx);
    }

    public Optional<TextNode> getAsTextNode() {
        return Type.TEXT.equals(type()) ? Optional.of((TextNode) this) : Optional.empty();
    }

    public Optional<BooleanNode> getAsBooleanNode() {
        return Type.BOOLEAN.equals(type()) ? Optional.of((BooleanNode) this) : Optional.empty();
    }

    public Optional<NumberNode> getAsNumberNode() {
        return Type.NUMBER.equals(type()) ? Optional.of((NumberNode) this) : Optional.empty();
    }

    public Optional<NullNode> getAsNullNode() {
        return Type.NULL.equals(type()) ? Optional.of((NullNode) this) : Optional.empty();
    }

    public Optional<ObjectNode> getAsObjectNode() {
        return Type.OBJECT.equals(type()) ? Optional.of((ObjectNode) this) : Optional.empty();
    }

    public Optional<ArrayNode> getAsArrayNode() {
        return Type.ARRAY.equals(type()) ? Optional.of((ArrayNode) this) : Optional.empty();
    }
}
