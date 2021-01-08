package parser;

import model.YtCfg;

import static parser.ModelMapper.parse;
import static parser.ParserUtil.*;

public class VideoPageBodyParser {

    public static class VideoPageSession {
        public final YtCfg ytCfg;
        public final String continuation;
        public final String itct;

        private VideoPageSession(YtCfg ytCfg, String continuation, String itct) {
            this.ytCfg = ytCfg;
            this.continuation = continuation;
            this.itct = itct;
        }
    }

    private static final String CONFIG_MARKER = "ytcfg.set\\(";
    private static final String ITEM_SECTION = "itemSectionRenderer";
    private static final String ITEM_SECTION_IDENTIFIER_KEY = "sectionIdentifier";
    private static final String ITEM_SECTION_IDENTIFIER_VALUE = "comment-item-section";
    private static final String ITEM_SECTION_CONTINUATION_KEY = "continuation";
    private static final String ITEM_SECTION_ITCT_KEY = "clickTrackingParams";

    public static VideoPageSession parseVideoPageBody(String body) {

        String ytCfgSoup = parseMarkedJsonObject(CONFIG_MARKER, body);

//        Map<String, Object> debug = parseJsSoup(ytCfgSoup);

        YtCfg ytCfg = parse(ytCfgSoup, YtCfg.class);

        String itemSection = parseNestedJsonObject(ITEM_SECTION, body);

        if (!ITEM_SECTION_IDENTIFIER_VALUE.equals(parseUniqueJsonEntry(ITEM_SECTION_IDENTIFIER_KEY, itemSection)))
            throw new RuntimeException("Invalid Item Section");

        String continuation = parseUniqueJsonEntry(ITEM_SECTION_CONTINUATION_KEY, itemSection);
        String itct = parseUniqueJsonEntry(ITEM_SECTION_ITCT_KEY, itemSection);

        return new VideoPageSession(
                ytCfg,
                continuation,
                itct
        );
    }
}
