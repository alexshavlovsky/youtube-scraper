package com.ctzn.youtubescraper.core.parser;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.model.YoutubeCfgDTO;
import com.ctzn.youtubescraper.core.model.browsev1.ClientContext;
import com.ctzn.youtubescraper.core.model.channelmetadata.ChannelMetadata;
import com.ctzn.youtubescraper.core.model.channelvideos.VideosGrid;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;

import static com.ctzn.youtubescraper.core.parser.ParserUtil.*;
import static com.ctzn.youtubescraper.core.parser.json.JsonMapper.parse;

public class VideoPageBodyParser {

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String INITIAL_DATA_MARKER = "var ytInitialData\\s*=\\s*";
    private static final String COMMENT_ITEM_SECTION_ENTRY_REGEX = "\"sectionIdentifier\"\\s*:\\s*\"comment-item-section\"";

    public String scrapeYoutubeConfigJson(String body) throws ScraperParserException {
        return parseMarkedJsonObject(CONFIG_MARKER, body);
    }

    public YoutubeCfgDTO parseYoutubeConfig(String ytCfgJson) throws ScraperParserException {
        return parse(ytCfgJson, YoutubeCfgDTO.class);
    }

    public ClientContext parseClientContext(String ytCfgJson) throws ScraperParserException {
        return parse(parseNestedJsonObject("INNERTUBE_CONTEXT", ytCfgJson), ClientContext.class);
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

    public VideosGrid parseVideosGrid(String ytInitialDataJson) throws ScraperParserException {
        String videosGridJson;
        try {
            videosGridJson = parseMarkedJsonObject("\"gridRenderer\":", ytInitialDataJson);
        } catch (ScraperParserException e) {
            // it seems the channel hasn't any videos yet
            return new VideosGrid(null, null);
        }
        return parse(videosGridJson, VideosGrid.class);
    }

    public String parseChannelVanityName(String vanityChannelUrl) throws ScraperParserException {
        return matchUniqueNamedMatcherGroup("www.youtube.com/(?<name>.+)$", "name", vanityChannelUrl);
    }
}
