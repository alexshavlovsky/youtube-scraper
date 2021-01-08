import parser.VideoPageBodyParser;
import http.VideoPageHttpClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static parser.VideoPageBodyParser.parseVideoPageBody;
import static http.CommentApiHttpClient.fetchComments;
import static http.VideoPageHttpClient.fetchVideoPage;

public class App {

    public static void main(String[] args) throws IOException {
        VideoPageHttpClient.HttpContext httpContext = fetchVideoPage("tcqbAaXBq2s");
        VideoPageBodyParser.VideoPageSession session = parseVideoPageBody(httpContext.body);
        httpContext = fetchComments(httpContext, session);
        Files.writeString(Path.of("comment_api_response.json"), httpContext.body);
//        String s = Files.readString(Path.of("6.txt"));
    }
}
