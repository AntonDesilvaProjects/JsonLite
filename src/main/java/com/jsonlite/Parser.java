package com.jsonlite;

import com.jsonlite.model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Parser {
    public JsonNode readTree(String json) {
        final String cleanJson = json.strip();
        // starts with { and ends with } => ObjectNode
        // starts with [ and ends with ] => ArrayNode
        // a String(e.g. "hello") with quotes => TextNode
        // either raw 'true' or 'false' => BooleanNode
        // 'null' => NullNode
        // if number => NumberNode
        JsonNode jsonNode = null;
        if (isText(cleanJson)) {
            jsonNode = new TextNode(cleanJson);
        } else if (isBoolean(cleanJson)) {
            jsonNode = new BooleanNode(Boolean.parseBoolean(cleanJson));
        } else if (isNull(cleanJson)) {
            jsonNode = new NullNode();
        } else if (isNumber(cleanJson)) {
            Number number;
            try {
                number = Long.parseLong(cleanJson);
            } catch (NumberFormatException nfe) {
                number = Double.parseDouble(cleanJson);
            }
            jsonNode = new NumberNode(number);
        } else if (isObject(cleanJson)) {
            String innerJson = cleanJson.substring(1, cleanJson.length() - 1);
            // split by commas that are not in {} or []
            String[] keyValues = innerJson.split(",(?![^{}\\[\\]]*\\})");
            Map<String, JsonNode> children = new HashMap<>();
            for (String keyValueString : keyValues) {
                final String[] keyValue = keyValueString.split(":", 2); // split only on the first :
                String key = keyValue[0].strip();
                String value = keyValue[1].strip();
                children.put(key, readTree(value));
            }
            jsonNode = new ObjectNode(children);
        } else if (isArray(cleanJson)) {
            String innerJson = cleanJson.substring(1, cleanJson.length() - 1);
            // split by commas that are not in {} or []
            String[] arrayElements = innerJson.split(",(?![^{}]*\\})");
            Map<String, JsonNode> children = new HashMap<>();
            for (int i = 0; i < arrayElements.length; i++) {
                children.put(String.valueOf(i), readTree(arrayElements[i]));
            }
            jsonNode = new ArrayNode(children);
        }
        return jsonNode;
    }

    private static boolean isText(String json) {
        return StringUtils.startsWith(json, "\"") && StringUtils.endWith(json, "\"");
    }

    private static boolean isBoolean(String json) {
        return "true".equals(json) || "false".equals(json);
    }

    private static boolean isNull(String json) {
        return "null".equals(json);
    }

    private static boolean isNumber(String json) {
       try {
           Double.parseDouble(json);
           return true;
       } catch (NumberFormatException nfe) {
           return false;
       }
    }

    private static boolean isObject(String json) {
        return StringUtils.startsWith(json, "{") && StringUtils.endWith(json, "}");
    }

    private static boolean isArray(String json) {
        return StringUtils.startsWith(json, "[") && StringUtils.endWith(json, "]");
    }

    private String getNextKeyValue(String json, int startPosition) {
        // ignore commas in {}, [], and ""
        Stack<Character> s = new Stack<>();
        // we need to make a special allowance for the " character
        // if the top character is a ", then nothing should be added or removed  to/from stack until
        // another " is met
        for (int i = startPosition; i < json.length(); i++) {
            char top = s.peek();
            char c = json.charAt(i);
            if ((c == '{' || c == '[' || c == '"') && top != '"') {
                s.push(c);
            }

            if ((c == '}' || c == ']' || c == '"')) {
                if (top != '"') {
                    if (top == '{' && c == '}') {
                        s.pop();
                    }
                    if (top == '[' && c == ']') {
                        s.pop();
                    }
                } else if (top == '"' && c == '"') {
                    s.pop();
                }
            }
            if (c == ',' && s.empty()) {
                return json.substring(startPosition, i);
            }
        }
        return null;
    }

}
