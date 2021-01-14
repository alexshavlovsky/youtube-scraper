package com.ctzn.youtubescraper.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserUtil {

    private static final String JSON_ENTRY_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\"(?<value>[^\"]+)\""; // ["key":"value"]
    private static final String JSON_NESTED_OBJECT_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\\{";  // ["key":{]
    private static final String JSON_MARKED_OBJECT_REGEX_TEMPLATE = "%s\\{";  // [marker{]

    private static int nextParenPos(String s, int i, char p1, char p2) throws Exception {
        int counter = 0;
        while (i < s.length()) {
            if (s.charAt(i) == p1) counter++;
            if (s.charAt(i) == p2 && --counter == 0) return i;
            i++;
        }
        throw new Exception("Error while looking for next paren");
    }

    private static int enclosingParenPos(String s, int i, char p1, char p2) throws Exception {
        int counter = 1;
        while (i >= 0) {
            if (s.charAt(i) == p1 && --counter == 0) return i;
            if (s.charAt(i) == p2) counter++;
            i--;
        }
        throw new Exception("Error while looking for enclosing paren");
    }

    private static int regexPos(String regex, String input) throws Exception {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.start();
        throw new Exception("Token not found: " + regex);
    }

    private static int prefixedObjectPos(String template, String key, String input) throws Exception {
        String regex = String.format(template, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.end() - 1;
        throw new Exception("Prefixed object not found: " + key);
    }

    private static String parsePrefixedObject(String template, String key, String input) throws Exception {
        int objectPos = prefixedObjectPos(template, key, input);
        int objectEnd = nextParenPos(input, objectPos, '{', '}');
        return input.substring(objectPos, objectEnd + 1);
    }

    static String parseNestedJsonObject(String key, String input) throws Exception {
        return parsePrefixedObject(JSON_NESTED_OBJECT_REGEX_TEMPLATE, key, input);
    }

    static String parseMarkedJsonObject(String marker, String input) throws Exception {
        return parsePrefixedObject(JSON_MARKED_OBJECT_REGEX_TEMPLATE, marker, input);
    }

    static String parseEnclosingObjectByEntryRegex(String regex, String input) throws Exception {
        int entryPos = regexPos(regex, input);
        int objectPos = enclosingParenPos(input, entryPos, '{', '}');
        int objectEnd = nextParenPos(input, objectPos, '{', '}');
        return input.substring(objectPos, objectEnd + 1);
    }

    private static String matchUniqueNamedMatcherGroup(String regex, String groupName, String input) throws Exception {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find() && matcher.groupCount() == 1) return matcher.group(groupName);
        throw new Exception("Error while matching unique matcher group: " + regex);
    }

    static String parseUniqueJsonEntry(String key, String input) throws Exception {
        String regex = String.format(JSON_ENTRY_REGEX_TEMPLATE, key);
        return matchUniqueNamedMatcherGroup(regex, "value", input);
    }

    static int parseDigitsToInt(String input) {
        String number = input.replaceAll("[^\\d]+", "");
        if (number.isEmpty()) return 0;
        return Integer.parseInt(number);
    }
}
