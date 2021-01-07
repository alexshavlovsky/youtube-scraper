package parser;

import java.util.Map;
import java.util.StringJoiner;

import static parser.ModelMapper.parseJsSoup;
import static parser.ParserUtil.parseNestedJsonObject;
import static parser.ParserUtil.parseUniqueJsonEntry;

public class VideoPageBodyParser {

    public static class VideoPageSession {
        public final String xsrfFieldName;
        public final String xsrfToken;
        public final String continuation;
        public final String itct;

        private VideoPageSession(String xsrfFieldName, String xsrfToken, String continuation, String itct) {
            this.xsrfFieldName = xsrfFieldName;
            this.xsrfToken = xsrfToken;
            this.continuation = continuation;
            this.itct = itct;
        }

        @Override
        public String toString() {
            return new StringJoiner(", ", VideoPageSession.class.getSimpleName() + "[", "]")
                    .add("xsrfFieldName='" + xsrfFieldName + "'")
                    .add("xsrfToken='" + xsrfToken + "'")
                    .add("continuation='" + continuation + "'")
                    .add("itct='" + itct + "'")
                    .toString();
        }
    }

    private static final String XSRF_FIELD_NAME_KEY = "XSRF_FIELD_NAME";
    private static final String XSRF_TOKEN_KEY = "XSRF_TOKEN";
    private static final String ITEM_SECTION = "itemSectionRenderer";
    private static final String ITEM_SECTION_IDENTIFIER_KEY = "sectionIdentifier";
    private static final String ITEM_SECTION_IDENTIFIER_VALUE = "comment-item-section";
    private static final String ITEM_SECTION_CONTINUATION_KEY = "continuation";
    private static final String ITEM_SECTION_ITCT_KEY = "clickTrackingParams";

    public static VideoPageSession parseVideoPageBody(String body) {


        //Map<String, Object> g = parseJsSoup(body);

        String xsrfFieldName = parseUniqueJsonEntry(XSRF_FIELD_NAME_KEY, body);
        String xsrfToken = parseUniqueJsonEntry(XSRF_TOKEN_KEY, body);

        String itemSection = parseNestedJsonObject(ITEM_SECTION, body);

        if (!ITEM_SECTION_IDENTIFIER_VALUE.equals(parseUniqueJsonEntry(ITEM_SECTION_IDENTIFIER_KEY, itemSection)))
            throw new RuntimeException("Invalid Item Section");

        String continuation = parseUniqueJsonEntry(ITEM_SECTION_CONTINUATION_KEY, itemSection);
        String itct = parseUniqueJsonEntry(ITEM_SECTION_ITCT_KEY, itemSection);

        return new VideoPageSession(
                xsrfFieldName,
                xsrfToken,
                continuation,
                itct
        );
    }
}
