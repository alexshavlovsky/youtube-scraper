package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.YoutubeCfgDTO;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseEnclosingObjectByEntryRegex;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseMarkedJsonObject;

public class VideoPageBodyParser {

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String INITIAL_CONFIG_MARKER = "var ytInitialData\\s*=\\s*";
    private static final String COMMENT_ITEM_SECTION_ENTRY_REGEX = "\"sectionIdentifier\"\\s*:\\s*\"comment-item-section\"";

    public YoutubeCfgDTO scrapeYoutubeConfig(String body) throws Exception {
        return parse(parseMarkedJsonObject(CONFIG_MARKER, body), YoutubeCfgDTO.class);
    }

    public CommentItemSection scrapeInitialCommentItemSection(String body) throws Exception {
        String ytInitialDataJson = parseMarkedJsonObject(INITIAL_CONFIG_MARKER, body);
        String commentItemSectionJson = parseEnclosingObjectByEntryRegex(COMMENT_ITEM_SECTION_ENTRY_REGEX, ytInitialDataJson);
        return parse(commentItemSectionJson, CommentItemSection.class);
    }
}
