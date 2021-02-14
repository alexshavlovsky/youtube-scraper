package com.ctzn.youtubescraper.model.comments;

import lombok.Value;

@Value
public class CommentDTO {
    public String videoId;
    public String commentId;
    public String authorText;
    public String channelId;
    public String publishedTimeText;
    public String text;
    public int likeCount;
    public int replyCount;
    public String parentCommentId;
}
