package com.jsonlite;

import com.jsonlite.model.JsonNode;

public class Runner {
    public static void main(String... args) {
        Parser parser = new Parser();

        String JSON = "1234";

        JsonNode json = parser.readTree(JSON);
        System.out.println(json.resolve("/3")
                .map(n -> n.toJsonString(true))
                .orElse("No Such JSON element!"));
    }

}
