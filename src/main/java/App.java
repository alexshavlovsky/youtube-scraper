import parser.VideoPageBodyParser;
import service.VideoPageHttpClient;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.apache.commons.text.StringEscapeUtils.unescapeJava;
import static parser.VideoPageBodyParser.parseVideoPageBody;
import static service.VideoPageHttpClient.fetchVideoPage;

public class App {

    public static void main(String[] args) throws IOException {
        VideoPageHttpClient.HttpContext context = fetchVideoPage("668nUCeBHyY");
        String s = unescapeJava(new String(context.body.readAllBytes(), StandardCharsets.UTF_8));

        VideoPageBodyParser.VideoPageSession session = parseVideoPageBody(unescapeJava(new String(context.body.readAllBytes(), StandardCharsets.UTF_8)));


//        try (PrintWriter out = new PrintWriter("4.txt")) {
//            out.println(s);
//        }

//        String s = Files.readString(Path.of("cfg.txt"));

//        fetchComments(context, session);
//
//        File file = Path.of("3.txt").toFile();
//        transferInputStreamToFile(context.body, file);

//        String s = Files.readString(Path.of("2.txt"));
//        System.out.println(parseVideoPageBody(s));
    }
}


