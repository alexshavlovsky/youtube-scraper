package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

import java.util.Date;

@Value
public class CommentDTO {
    public String videoId;
    public String commentId;
    public String authorText;
    public String channelId;
    public String publishedTimeText;
    public Date publishedDate;
    public String text;
    public int likeCount;
    public int replyCount;
    public String parentCommentId;
}
