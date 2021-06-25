package com.jsonlite;

import com.jsonlite.model.JsonNode;
import com.jsonlite.model.NumberNode;

public class Runner {
    public static void main(String... args) {
        Parser parser = new Parser();
        JsonNode json = parser.readTree("{\n" +
                "    \"name\": \"Reeki\",\n" +
                "    \"age\": 28,\n" +
                "    \"profession\": {\n" +
                "        \"title\": \"Public Health Advisor\",\n" +
                "        \"pay\": 50000\n" +
                "    },\n" +
                "    \"alias\": [\n" +
                "        \"rika\",\n" +
                "        \"peprika\"\n" +
                "    ],\n" +
                "    \"isStudent\": false\n" +
                "}");
        System.out.println(json.type());

    }

}
