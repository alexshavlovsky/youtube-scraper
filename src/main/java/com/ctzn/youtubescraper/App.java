package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.http.YoutubeHttpClient;
import com.ctzn.youtubescraper.model.CommentItemSection;

public class App {

    public static void main(String[] args) {
        YoutubeHttpClient client = new YoutubeHttpClient("gdZLi9oWNZg");
        while (client.hasComments()) {
            CommentItemSection comments = client.nextComments();
            comments.printComments();
        }
    }
}
