import http.VideoPageHttpClient;
import parser.VideoPageBodyParser;

import java.io.IOException;

import static http.CommentApiHttpClient.fetchComments;
import static http.VideoPageHttpClient.fetchVideoPage;
import static parser.VideoPageBodyParser.parseCommentApiResponse;
import static parser.VideoPageBodyParser.scrapeYoutubeContext;

public class App {

    public static void main(String[] args) {
        VideoPageHttpClient.HttpContext httpContext = fetchVideoPage("tcqbAaXBq2s");
        VideoPageBodyParser.YoutubeContext youtubeContext = scrapeYoutubeContext(httpContext.body);
        while (youtubeContext.commentItemSection.hasContinuation()) {
            httpContext = fetchComments(httpContext, youtubeContext);
            youtubeContext = parseCommentApiResponse(httpContext.body, youtubeContext);
            youtubeContext.commentItemSection.printComments();
        }
    }
}
