package com.jsonlite.model;

public class NullNode extends JsonNode {
    public NullNode() {
        super(null, null);
    }

    @Override
    public Type type() {
        return Type.NULL;
    }
}
