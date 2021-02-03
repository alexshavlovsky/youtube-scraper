package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtil {

    private static final String JSON_ENTRY_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\"(?<value>[^\"]+)\""; // ["key":"value"]
    private static final String JSON_NESTED_OBJECT_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\\{";  // ["key":{]
    private static final String JSON_MARKED_OBJECT_REGEX_TEMPLATE = "%s\\{";  // [marker{]

    private static int nextParenPos(String s, int i, char p1, char p2) throws ScraperParserException {
        int counter = 0;
        while (i < s.length()) {
            if (s.charAt(i) == p1) counter++;
            if (s.charAt(i) == p2 && --counter == 0) return i;
            i++;
        }
        throw new ScraperParserException("Next paren not found");
    }

    private static int enclosingParenPos(String s, int i, char p1, char p2) throws ScraperParserException {
        int counter = 1;
        while (i >= 0) {
            if (s.charAt(i) == p1 && --counter == 0) return i;
            if (s.charAt(i) == p2) counter++;
            i--;
        }
        throw new ScraperParserException("Enclosing paren not found");
    }

    private static int regexPos(String regex, String input) throws ScraperParserException {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.start();
        throw new ScraperParserException("Token not found: regex = [%s], input = [%s]", regex, input);
    }

    private static int prefixedObjectPos(String template, String key, String input) throws ScraperParserException {
        String regex = String.format(template, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.end() - 1;
        throw new ScraperParserException("Prefixed object not found: template = [%s], key = [%s]", template, key);
    }

    private static String parsePrefixedObject(String template, String key, String input) throws ScraperParserException {
        int objectPos = prefixedObjectPos(template, key, input);
        int objectEnd = nextParenPos(input, objectPos, '{', '}');
        return input.substring(objectPos, objectEnd + 1);
    }

    static String parseNestedJsonObject(String key, String input) throws ScraperParserException {
        return parsePrefixedObject(JSON_NESTED_OBJECT_REGEX_TEMPLATE, key, input);
    }

    static String parseMarkedJsonObject(String marker, String input) throws ScraperParserException {
        return parsePrefixedObject(JSON_MARKED_OBJECT_REGEX_TEMPLATE, marker, input);
    }

    static String parseEnclosingObjectByEntryRegex(String regex, String input) throws ScraperParserException {
        int entryPos = regexPos(regex, input);
        int objectPos = enclosingParenPos(input, entryPos, '{', '}');
        int objectEnd = nextParenPos(input, objectPos, '{', '}');
        return input.substring(objectPos, objectEnd + 1);
    }

    static String matchUniqueNamedMatcherGroup(String regex, String groupName, String input) throws ScraperParserException {
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find() && matcher.groupCount() == 1) return matcher.group(groupName);
        throw new ScraperParserException("Unique named matcher group not found: regex = [%s], groupName = [%s]", regex, groupName);
    }

    static String parseUniqueJsonEntry(String key, String input) throws ScraperParserException {
        String regex = String.format(JSON_ENTRY_REGEX_TEMPLATE, key);
        return matchUniqueNamedMatcherGroup(regex, "value", input);
    }

    public static int parseDigitsToInt(String input) {
        String number = input.replaceAll("[^\\d]+", "");
        if (number.isEmpty()) return 0;
        return Integer.parseInt(number);
    }

    public static void assertNotNull(String message, Object... objects) throws ScraperParserException {
        for (Object object : objects) if (object == null) throw new ScraperParserException(message);
    }
}
