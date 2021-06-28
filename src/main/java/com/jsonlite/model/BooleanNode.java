package com.jsonlite.model;

public class BooleanNode extends JsonNode {

    private final boolean booleanValue;

    public BooleanNode(boolean value) {
        super(value, null);
        this.booleanValue = value;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    @Override
    public Type type() {
        return Type.BOOLEAN;
    }

    @Override
    public String toJsonString(boolean prettyPrint) {
        return Boolean.toString(getBooleanValue());
    }

    @Override
    protected String toJsonString(int indent) {
        return toJsonString(false);
    }
}
