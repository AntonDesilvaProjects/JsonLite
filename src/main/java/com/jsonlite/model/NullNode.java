package com.jsonlite.model;

public class NullNode extends JsonNode {
    public NullNode() {
        super(null, null);
    }

    @Override
    public Type type() {
        return Type.NULL;
    }

    @Override
    public String toJsonString(boolean prettyPrint) {
        return "null";
    }

    @Override
    protected String toJsonString(int indent) {
        return toJsonString(false);
    }
}
