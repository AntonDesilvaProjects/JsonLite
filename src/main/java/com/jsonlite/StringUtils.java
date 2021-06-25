package com.jsonlite;

public class StringUtils {
    public static boolean startsWith(String string, String prefix) {
        if (string == null) {
            return prefix == null;
        }
        if (prefix == null) {
            return false;
        }
        return string.startsWith(prefix);
    }

    public static boolean endWith(String string, String suffix) {
        if (string == null) {
            return suffix == null;
        }
        if (suffix == null) {
            return false;
        }
        return string.endsWith(suffix);
    }
}
