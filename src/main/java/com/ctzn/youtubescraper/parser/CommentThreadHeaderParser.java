package com.ctzn.youtubescraper.parser;

import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;

public class CommentThreadHeaderParser {
    public int parseCommentsCountText(String text) {
        return parseDigitsToInt(text);
    }
}
