package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.model.YoutubeConfig;
import com.ctzn.youtubescraper.model.commentitemsection.CommentItemSection;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseEnclosingObjectByEntryRegex;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseMarkedJsonObject;

public class VideoPageBodyParser {

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String INITIAL_CONFIG_MARKER = "var ytInitialData\\s*=\\s*";
    private static final String COMMENT_ITEM_SECTION_ENTRY_REGEX = "\"sectionIdentifier\"\\s*:\\s*\"comment-item-section\"";

    public static YoutubeConfig scrapeYoutubeConfig(String body) {
        return parse(parseMarkedJsonObject(CONFIG_MARKER, body), YoutubeConfig.class);
    }

    public static CommentItemSection scrapeInitialCommentItemSection(String body) {
        String ytInitialDataJson = parseMarkedJsonObject(INITIAL_CONFIG_MARKER, body);
        String commentItemSectionJson = parseEnclosingObjectByEntryRegex(COMMENT_ITEM_SECTION_ENTRY_REGEX, ytInitialDataJson);
        return parse(commentItemSectionJson, CommentItemSection.class);
    }
}
