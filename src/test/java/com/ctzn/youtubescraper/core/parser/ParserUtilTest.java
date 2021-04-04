package com.ctzn.youtubescraper.core.parser;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class ParserUtilTest {

    final private static String RESPONSE_ARRAY_ENTRY_REGEX = "\"xsrf_token\"\\s*:\\s*\"";

    private static String loadSample(int n) throws IOException {
        return new String(
                Files.readAllBytes(Paths.get(String.format("src/test/resources/test_data/raw_comment_api_response%d.txt", n)))
        );
    }

    private static void doAssert(String input, int expected) throws ScraperParserException {
        int entryPos = ParserUtil.regexPos(RESPONSE_ARRAY_ENTRY_REGEX, input);
        Assertions.assertDoesNotThrow(() -> {
            int actual = ParserUtil.nextParenPos(input, entryPos, '{', '}');
            Assertions.assertEquals(expected, actual);
        });
    }

    @Test
    void testNextParenPos() {
        Assertions.assertThrows(ScraperParserException.class, () -> ParserUtil.nextParenPos("{", 0, '{', '}'));
        Assertions.assertThrows(ScraperParserException.class, () -> ParserUtil.nextParenPos("{\"}", 0, '{', '}'));
        Assertions.assertDoesNotThrow(() -> {
            Assertions.assertEquals(1, ParserUtil.nextParenPos("{}", 0, '{', '}'));
            Assertions.assertEquals(14, ParserUtil.nextParenPos("{\"key\":\"value\"}", 0, '{', '}'));
            Assertions.assertEquals(16, ParserUtil.nextParenPos("{\"\\\"key\":\"value\"}", 0, '{', '}'));
            Assertions.assertEquals(16, ParserUtil.nextParenPos("{\"key\\\"\":\"value\"}", 0, '{', '}'));
            Assertions.assertEquals(14, ParserUtil.nextParenPos("{\"k}y\":\"value\"}", 0, '{', '}'));
            Assertions.assertEquals(14, ParserUtil.nextParenPos("{\"k{y\":\"value\"}", 0, '{', '}'));
        });
    }

    @Test
    void testNextParenSample1() {
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertEquals(128756, ParserUtil.nextParenPos(loadSample(1), 0, '{', '}')));
    }

    @Test
    void testNextParenSample2() {
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertEquals(134066, ParserUtil.nextParenPos(loadSample(2), 0, '{', '}')));
    }

    @Test
    void testNextParenSample3() {
        Assertions.assertDoesNotThrow(() ->
                Assertions.assertEquals(134816, ParserUtil.nextParenPos(loadSample(3), 0, '{', '}')));
    }

    @Test
    void testNextParenSample5() throws IOException {
        String input = loadSample(5);
        Assertions.assertDoesNotThrow(() -> doAssert(input, 20296));
    }

    @Test
    void testNextParenSample6() throws IOException {
        String input = loadSample(6);
        Assertions.assertDoesNotThrow(() -> doAssert(input, 32411));
    }

}
