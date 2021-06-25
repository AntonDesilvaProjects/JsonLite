package com.jsonlite.model;

public class NumberNode extends JsonNode {
    private final Number number;

    public NumberNode(Number number) {
        super(number,null);
        this.number = number;
    }

    public Number getNumber() {
        return number;
    }

    @Override
    public Type type() {
        return Type.NUMBER;
    }
}
