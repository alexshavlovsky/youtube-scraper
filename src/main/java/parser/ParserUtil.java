package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserUtil {

    private static final String JSON_ENTRY_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\"([^\"]+)\""; // ["key":"value"]
    private static final String JSON_NESTED_OBJECT_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\\{";  // ["key":{]
    private static final String JSON_MARKED_OBJECT_REGEX_TEMPLATE = "%s\\{";  // [marker{]

    private static int nextParenPos(String s, int i, char p1, char p2) {
        int counter = 0;
        while (i < s.length()) {
            if (s.charAt(i) == p1) counter++;
            if (s.charAt(i) == p2 && --counter == 0) return i;
            i++;
        }
        throw new RuntimeException();
    }

    private static int prefixedObjectPos(String template, String key, String input) {
        String regex = String.format(template, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.end() - 1;
        throw new RuntimeException();
    }

    private static String parsePrefixedObject(String template, String key, String input) {
        int z = prefixedObjectPos(template, key, input);
        int y = nextParenPos(input, z, '{', '}');
        return input.substring(z, y + 1);
    }

    static String parseNestedJsonObject(String key, String input) {
        return parsePrefixedObject(JSON_NESTED_OBJECT_REGEX_TEMPLATE, key, input);
    }

    static String parseMarkedJsonObject(String marker, String input) {
        return parsePrefixedObject(JSON_MARKED_OBJECT_REGEX_TEMPLATE, marker, input);
    }

    static String parseUniqueJsonEntry(String key, String input) {
        String regex = String.format(JSON_ENTRY_REGEX_TEMPLATE, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find() && matcher.groupCount() == 1) return matcher.group(1);
        throw new RuntimeException();
    }
}
