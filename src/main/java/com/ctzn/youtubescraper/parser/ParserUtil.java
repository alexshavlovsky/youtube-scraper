package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserUtil {

    private static final String JSON_ENTRY_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\"(?<value>[^\"]+)\""; // ["key":"value"]
    private static final String JSON_NESTED_OBJECT_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\\{";  // ["key":{]
    private static final String JSON_MARKED_OBJECT_REGEX_TEMPLATE = "%s\\{";  // [marker{]

    // TODO refactor this to make it error-prone
    // maybe use json parser library
    static int nextParenPos(String s, int i0, char p1, char p2) throws ScraperParserException {
        int counter = 0, i = i0;
        while (i < s.length()) {
            if (s.charAt(i) == '"') {
                i = skipJsonString(s, i);
                if (i == -1) break;
                else continue;
            }
            if (s.charAt(i) == p1) counter++;
            if (s.charAt(i) == p2 && --counter == 0) return i;
            i++;
        }
        throw new ScraperParserException(String.format("Next paren not found from pos (%d): %s", i0, s));
    }

    private static int skipJsonString(String s, int i) {
        i++;
        while (i < s.length()) {
            if (i < s.length() - 1 && s.charAt(i) == '\\') {
                i += 2;
                continue;
            }
            if (s.charAt(i) == '"') return i + 1;
            i++;
        }
        return -1;
    }

    private static int enclosingParenPos(String s, int i, char p1, char p2) throws ScraperParserException {
        // TODO fix this to handle json strings
        int counter = 1;
        while (i >= 0) {
            if (s.charAt(i) == p1 && --counter == 0) return i;
            if (s.charAt(i) == p2) counter++;
            i--;
        }
        throw new ScraperParserException("Enclosing paren not found");
    }

    static int regexPos(String regex, String input) throws ScraperParserException {
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

    public static long parseSubCount(String input) {
        Map<String, Integer> multiplier = Map.of("", 1, "K", 1_000, "M", 1_000_000, "B", 1_000_000_000);
        Matcher matcher = Pattern.compile("([\\d.]+)([KM]?+)").matcher(input);
        if (matcher.find() && matcher.groupCount() == 2)
            return Math.round(Double.parseDouble(matcher.group(1)) * multiplier.get(matcher.group(2)));
        return 0;
    }

    public static void assertNotNull(String message, Object... objects) throws ScraperParserException {
        for (Object object : objects) if (object == null) throw new ScraperParserException(message);
    }
}
