package parser;

import model.YoutubeConfig;
import model.commentapiresponse.CommentApiResponse;
import model.commentitemsection.CommentItemSection;

import static parser.ModelMapper.parse;
import static parser.ParserUtil.parseEnclosingObjectByEntryRegex;
import static parser.ParserUtil.parseMarkedJsonObject;

public class VideoPageBodyParser {

    public static class YoutubeContext {
        public final YoutubeConfig youtubeConfig;
        public final CommentItemSection commentItemSection;

        private YoutubeContext(YoutubeConfig youtubeConfig, CommentItemSection commentItemSection) {
            this.youtubeConfig = youtubeConfig;
            this.commentItemSection = commentItemSection;
        }
    }

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String INITIAL_CONFIG_MARKER = "var ytInitialData\\s*=\\s*";
    private static final String COMMENT_ITEM_SECTION_ENTRY_REGEX = "\"sectionIdentifier\"\\s*:\\s*\"comment-item-section\"";

    public static YoutubeContext scrapeYoutubeContext(String body) {
        YoutubeConfig youtubeConfig = parse(parseMarkedJsonObject(CONFIG_MARKER, body), YoutubeConfig.class);

        String ytInitialDataJson = parseMarkedJsonObject(INITIAL_CONFIG_MARKER, body);
        String commentItemSectionJson = parseEnclosingObjectByEntryRegex(COMMENT_ITEM_SECTION_ENTRY_REGEX, ytInitialDataJson);
        CommentItemSection commentItemSection = parse(commentItemSectionJson, CommentItemSection.class);

        return new YoutubeContext(youtubeConfig, commentItemSection);
    }

    public static YoutubeContext parseCommentApiResponse(String responseBody, YoutubeContext youtubeContext) {
        CommentApiResponse commentApiResponse = parse(responseBody, CommentApiResponse.class);
        CommentItemSection commentItemSection = commentApiResponse.getCommentItemSection();
        youtubeContext.youtubeConfig.setXSRF_TOKEN(commentApiResponse.getXsrf_token());

        return new YoutubeContext(youtubeContext.youtubeConfig, commentItemSection);
    }
}
