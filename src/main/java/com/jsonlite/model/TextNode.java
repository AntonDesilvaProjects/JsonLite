package com.jsonlite.model;

public class TextNode extends JsonNode {

    private final String textValue;

    public TextNode(String value) {
        super(value, null);
        this.textValue = value;
    }

    public String getText() {
        return textValue;
    }

    @Override
    public Type type() {
        return Type.TEXT;
    }

    @Override
    public String toJsonString(boolean prettyPrint) {
        return getText();
    }

    @Override
    protected String toJsonString(int indent) {
       return toJsonString(false);
    }
}
