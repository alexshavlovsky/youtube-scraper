import http.YoutubeHttpClient;
import model.commentitemsection.CommentItemSection;

public class App {

    public static void main(String[] args) {
        YoutubeHttpClient client = new YoutubeHttpClient("tcqbAaXBq2s");
        while (client.hasComments()) {
            CommentItemSection comments = client.nextComments();
            comments.printComments();
        }
    }
}
