package com.ctzn.youtubescraper.parser;

import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.model.CommentItemSection;
import com.ctzn.youtubescraper.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.model.channelmetadata.ChannelMetadata;

import static com.ctzn.youtubescraper.parser.ModelMapper.parse;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseEnclosingObjectByEntryRegex;
import static com.ctzn.youtubescraper.parser.ParserUtil.parseMarkedJsonObject;

public class VideoPageBodyParser {

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String INITIAL_DATA_MARKER = "var ytInitialData\\s*=\\s*";
    private static final String COMMENT_ITEM_SECTION_ENTRY_REGEX = "\"sectionIdentifier\"\\s*:\\s*\"comment-item-section\"";

    public YoutubeCfgDTO scrapeYoutubeConfig(String body) throws ScraperParserException {
        return parse(parseMarkedJsonObject(CONFIG_MARKER, body), YoutubeCfgDTO.class);
    }

    public String scrapeYtInitialDataJson(String body) throws ScraperParserException {
        return parseMarkedJsonObject(INITIAL_DATA_MARKER, body);
    }

    public CommentItemSection scrapeInitialCommentItemSection(String ytInitialDataJson) throws ScraperParserException {
        String commentItemSectionJson = parseEnclosingObjectByEntryRegex(COMMENT_ITEM_SECTION_ENTRY_REGEX, ytInitialDataJson);
        return parse(commentItemSectionJson, CommentItemSection.class);
    }

    public ChannelMetadata parseChannelMetadata(String ytInitialDataJson) throws ScraperParserException {
        return parse(ytInitialDataJson, ChannelMetadata.class);
    }
}
