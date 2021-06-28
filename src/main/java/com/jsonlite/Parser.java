package com.jsonlite;

import com.jsonlite.model.*;

import java.util.*;

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
            // split by commas that are not in {} or []
            List<String> keyValues = getJsonEntries(cleanJson);//innerJson.split(",(?![^{}\\[\\]]*\\})");
            Map<String, JsonNode> children = new HashMap<>();
            for (String keyValueString : keyValues) {
                final String strippedKeyValueString = keyValueString.strip();
                // "" means an empty object(i.e. {}) with no children to add
                if (!"".equals(strippedKeyValueString)) {
                    final String[] keyValue = strippedKeyValueString.split(":", 2); // split only on the first :
                    String key = keyValue[0].strip();
                    if (!(key.startsWith("\"") && key.endsWith("\""))) {
                        throw new IllegalArgumentException("Invalid JSON property name " + key);
                    } else {
                        key = key.substring(1, key.length() - 1);
                    }
                    String value = keyValue[1].strip();
                    children.put(key, readTree(value));
                }
            }
            jsonNode = new ObjectNode(children);
        } else if (isArray(cleanJson)) {
            // split by commas that are not in {} or []
            List<String> arrayElements = getJsonEntries(cleanJson);//innerJson.split(",(?![^{}]*\\})");
            Map<String, JsonNode> children = new HashMap<>();
            for (int i = 0; i < arrayElements.size(); i++) {
                children.put(String.valueOf(i), readTree(arrayElements.get(i)));
            }
            jsonNode = new ArrayNode(children);
        } else {
            throw new IllegalArgumentException("Invalid JSON!");
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

    public static String getNextKeyValue(String json, int startPosition) {
        // find the "next" comma ignoring any commas inside {}, [], and ""
        Stack<Character> s = new Stack<>();
        // we need to make a special allowance for the " character
        // if the top character is a ", then nothing should be added or removed  to/from stack until
        // another " is met
        // { "name": "test" }
        // { "name": {} }
        // { "name": [] }
        // { "name": "{}" }
        for (int i = startPosition; i < json.length(); i++) {
            boolean isEmpty = s.isEmpty();
            boolean isTopCharQuotation = !isEmpty && s.peek() == '"';

            char c = json.charAt(i);
            if ((c == '{' || c == '[' || c == '"') && !isTopCharQuotation) {
                s.push(c);
            }
            else if (c == '}' || c == ']' || c == '"') {
                if (isEmpty) {
                    // should not happen
                    throw new IllegalArgumentException("Invalid JSON!");
                }
                char top = s.peek();
                if (top == '{' && c == '}') {
                    s.pop();
                } else if (top == '[' && c == ']') {
                    s.pop();
                } else if (top == '"' && c == '"') {
                    s.pop();
                } else {
                    throw new IllegalArgumentException("Unbalanced JSON!");
                }
            }
            if (c == ',' && isEmpty) {
                return json.substring(startPosition, i);
            }
        }
        return json.substring(startPosition);
    }

    public static int getNextCommaIndex(String json, int startPosition) {
        // find the "next" comma ignoring any commas inside {}, [], and ""
        Stack<Character> openCharStack = new Stack<>();
        // we need to make a special allowance for the " character
        // if the top character is a ", then nothing should be added or removed  to/from stack until
        // another " is met
        for (int i = startPosition; i < json.length(); i++) {
            boolean isStackEmpty = openCharStack.isEmpty();
            boolean isLastOpeningCharQuote = !isStackEmpty && openCharStack.peek() == '"';

            char currentChar = json.charAt(i);
            if ((currentChar == '{' || currentChar == '[' || currentChar == '"') && !isLastOpeningCharQuote) {
                openCharStack.push(currentChar);
            }
            else if (currentChar == '}' || currentChar == ']' || currentChar == '"') {
                if (isStackEmpty) {
                    // a 'closing' character when there is no 'opening' character
                    throw new IllegalArgumentException(String.format("Encountered unbalanced character '%s' at index '%s'", currentChar, i));
                }
                char lastOpeningChar = openCharStack.peek();
                if (!isLastOpeningCharQuote) {
                    if (lastOpeningChar == '{' && currentChar == '}') {
                        openCharStack.pop();
                    } else if (lastOpeningChar == '[' && currentChar == ']') {
                        openCharStack.pop();
                    } else {
                        throw new IllegalArgumentException(String.format("Encountered unmatched character '%s' at index '%s'. Expected '%s'", currentChar, i, lastOpeningChar));
                    }
                } else if (currentChar == '"') {
                    openCharStack.pop();
                }
            }
            if (currentChar == ',' && isStackEmpty) {
                return i;
            }
        }
        return -1;
    }

    private List<String> getJsonEntries(String json) {
        List<String> jsonEntries = new ArrayList<>();
        // strip out the leading/trailing characters({}/[])
        String innerJson = json.substring(1, json.length() - 1);
        int startIdx = 0;
        while (true) {
            int nextComma = getNextCommaIndex(innerJson, startIdx);
            if (nextComma == -1) {
               jsonEntries.add(innerJson.substring(startIdx));
               break;
            } else {
               jsonEntries.add(innerJson.substring(startIdx, nextComma));
            }
            startIdx = nextComma + 1;
        }
        return jsonEntries;
    }

}
