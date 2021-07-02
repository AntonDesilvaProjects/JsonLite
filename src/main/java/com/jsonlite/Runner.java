package com.jsonlite;

import com.jsonlite.model.ArrayNode;
import com.jsonlite.model.JsonNode;
import com.jsonlite.model.NumberNode;
import com.jsonlite.model.TextNode;

import java.util.List;
import java.util.stream.Collectors;

public class Runner {
    public static void main(String... args) {
        Parser parser = new Parser();

        String JSON = "{\n" +
                "    \"name\" : \"Test\",\n" +
                "    \"records\" : [\n" +
                "        {\n" +
                "            \"id\": {\n" +
                "                \"namespace\" : \"namespace-a\",\n" +
                "                \"entityId\": 123\n" +
                "            },\n" +
                "            \"parents\" : [\n" +
                "                \"a\",\n" +
                "                \"b\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": {\n" +
                "                \"namespace\" : \"namespace-b\",\n" +
                "                \"entityId\": 456\n" +
                "            },\n" +
                "            \"parents\" : [\n" +
                "                \"a\",\n" +
                "                \"e\"\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"id\": {\n" +
                "                \"namespace\" : \"namespace-c\",\n" +
                "                \"entityId\": 789\n" +
                "            },\n" +
                "            \"parents\" : [\n" +
                "                \"f\",\n" +
                "                \"g\"\n" +
                "            ]\n" +
                "        }\n" +
                "    ]\n" +
                "}";

        JsonNode json = parser.readTree(JSON);
        List<JsonNode> searchResults = json.resolve("/records")
                .flatMap(JsonNode::getAsArrayNode)
                .map(node -> node.query(
                        n -> n.resolve("/id/entityId").flatMap(JsonNode::getAsNumberNode)
                                .map(k -> k.getNumber().intValue() == 789 || k.getNumber().intValue() == 123)
                                .orElse(false)))
                .orElse(null);
        System.out.println(searchResults == null ? null : searchResults.stream().map(node -> node.toJsonString(true)).collect(Collectors.toList()));
        //System.out.println(((ArrayNode) json.resolve("/records").get()).query(n -> n.resolve("/id/entityId").get()));
    }

}
