package com.jsonlite;

import com.jsonlite.model.JsonNode;

public class Runner {
    public static void main(String... args) {
        Parser parser = new Parser();
        JsonNode json = parser.readTree("[1, 2, \"test\", {\"hello\" : \"testy\"              }]");
        System.out.println(json.toJsonString(false));

//        String JSON = "\n" +
//                "\t\"name\": \"Anton\",\n" +
//                "\t\"options\": {\n" +
//                "\t\t\"age\": \"27\",\n" +
//                "\t\t\"profession\": \"Software Developer, IT\",\n" +
//                "\t\t\"alias\": [\"anton\", \"ads\", \"spinet\"],\n" +
//                "\t\t\"code\": {\n" +
//                "\t\t\t\"text\": \"{'name'}, ['age']\"\n" +
//                "\t\t}\n" +
//                "\t},\n" +
//                "\t\"nasty\": [\"[\", \"]\", \"{}\"],\n" +
//                "\t\"list\": [{\n" +
//                "\t\t\t\"stuff\": \"stuff, inc\"\n" +
//                "\t\t},\n" +
//                "\t\t[1, 2, 4]\n" +
//                "\t]\n" +
//                " ";
//        System.out.println(JSON);
//        int startIdx = 0;
//        while (true) {
//            int nextComma = Parser.getNextCommaIndex(JSON, startIdx);
//            if (nextComma == -1) {
//                System.out.println(JSON.substring(startIdx));
//                break;
//            } else {
//                System.out.println(JSON.substring(startIdx, nextComma));
//            }
//            System.out.println("====");
//            startIdx = nextComma + 1;
//        }
    }

}
