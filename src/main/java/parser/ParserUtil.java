package parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

class ParserUtil {

    private static final String JSON_ENTRY_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\"([^\"]+)\"";
    private static final String JSON_NESTED_OBJECT_REGEX_TEMPLATE = "\"%s\"\\s*:\\s*\\{";

    private static int nextParenPos(String s, int i, char p1, char p2) {
        int counter = 0;
        while (i < s.length()) {
            if (s.charAt(i) == p1) counter++;
            if (s.charAt(i) == p2 && --counter == 0) return i;
            i++;
        }
        throw new RuntimeException();
    }

    private static int nestedObjectPos(String key, String input) {
        String regex = String.format(JSON_NESTED_OBJECT_REGEX_TEMPLATE, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find()) return matcher.end() - 1;
        throw new RuntimeException();
    }

    static String parseNestedJsonObject(String key, String input) {
        int z = nestedObjectPos(key, input);
        int y = nextParenPos(input, z, '{', '}');
        return input.substring(z, y + 1);
    }

    static String parseUniqueJsonEntry(String key, String input) {
        String regex = String.format(JSON_ENTRY_REGEX_TEMPLATE, key);
        Matcher matcher = Pattern.compile(regex).matcher(input);
        if (matcher.find() && matcher.groupCount() == 1) return matcher.group(1);
        throw new RuntimeException();
    }
}
