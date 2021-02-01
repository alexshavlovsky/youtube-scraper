package com.ctzn.youtubescraper.model.comments;

public interface ApiResponse {
    CommentItemSection getItemSection();

    String getToken();
}
