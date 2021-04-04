package com.ctzn.youtubescraper.core.model.comments;

public interface ApiResponse {
    CommentItemSection getItemSection();

    String getToken();
}
