package com.ctzn.youtubescraper.core.model.comments;

import com.ctzn.youtubescraper.core.model.commons.*;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;

import java.util.*;

public class CommentItemSection {
    public List<Content> contents;
    public List<Continuation> continuations;
//    public String trackingParams;
    public SectionHeader header;
    public String sectionIdentifier;

    public boolean hasHeader() {
        return header != null;
    }

    public boolean hasContent() {
        return contents != null && !contents.isEmpty();
    }

    public boolean hasContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public boolean hasReplies() {
        return sumReplyCounters() > 0;
    }

    public int countContentPieces() {
        return hasContent() ? contents.size() : 0;
    }

    public NextContinuationData getContinuation() {
        if (!hasContinuation()) throw new IllegalStateException("The section hasn't continuation data");
        return continuations.get(0).nextContinuationData;
    }

    public SectionHeaderDTO getHeader() {
        if (!hasHeader()) throw new IllegalStateException("The section hasn't a header");
        return header.getValue();
    }

    public int sumReplyCounters() {
        if (!hasContent()) return 0;
        return contents.stream().mapToInt(c -> c.getCommentRenderer().replyCount).sum();
    }

    public int countReplyContinuations() {
        if (!hasContent()) return 0;
        return contents.stream().map(c -> c.commentThreadRenderer)
                .mapToInt(c -> c.replies == null ? 0 : c.replies.commentRepliesRenderer.continuations.size()).sum();
    }

    // keys - commentId
    // values - reply continuations
    public Map<String, NextContinuationData> getReplyContinuationMap() {
        if (!hasContent()) return Collections.emptyMap();
        Map<String, NextContinuationData> map = new HashMap<>();
        for (Content content : contents) {
            if (content.commentThreadRenderer == null)
                continue; // this is a reply continuation section and reply can't be replied
            CommentThreadRenderer commentContext = content.commentThreadRenderer;
            if (commentContext.replies != null) {
                CommentRenderer comment = commentContext.comment.commentRenderer;
                CommentRepliesRenderer replies = commentContext.replies.commentRepliesRenderer;
                map.put(comment.commentId, replies.continuations.get(0).nextContinuationData);
            }
        }
        return map;
    }

    public List<CommentDTO> getComments(String videoId, String parentCommentId) {
        if (!hasContent()) return Collections.emptyList();
        List<CommentDTO> list = new ArrayList<>();
        for (Content c : contents) {
            CommentRenderer r = c.getCommentRenderer();
            CommentDTO commentDTO = new CommentDTO(
                    videoId,
                    r.commentId,
                    r.authorText == null ? "" : r.authorText.toString(),
                    r.authorEndpoint == null ? "" : r.authorEndpoint.browseEndpoint.browseId,
                    r.publishedTimeText.toString(),
                    r.contentText.toString(),
                    r.likeCount,
                    r.replyCount,
                    parentCommentId
            );
            list.add(commentDTO);
        }
        return list;
    }

    static class Content {
        public CommentThreadRenderer commentThreadRenderer;
        public CommentRenderer commentRenderer;

        public CommentRenderer getCommentRenderer() {
            return commentThreadRenderer != null ? commentThreadRenderer.comment.commentRenderer : commentRenderer;
        }
    }

    static class CommentTargetTitle {
        public String simpleText;
    }

    static class BrowseEndpoint {
        public String browseId;
        public String canonicalBaseUrl;
    }

    static class CommentRenderer {
        public SimpleText authorText;
        public Thumbnails authorThumbnail;
        public AuthorEndpoint authorEndpoint;
        public Text contentText;
        public Text publishedTimeText;
        public boolean isLiked;
        public int likeCount;
        public String commentId;
        public boolean authorIsChannelOwner;
        public String voteStatus;
//        public String trackingParams;
        public SimpleText voteCount;
        public int replyCount;
    }

    static class CommentThreadRenderer {
        public Comment comment;
        public Replies replies;
        public CommentTargetTitle commentTargetTitle;
//        public String trackingParams;
        public String renderingPriority;
    }

    static class AuthorEndpoint {
        public String clickTrackingParams;
        public CommandMetadata commandMetadata;
        public BrowseEndpoint browseEndpoint;
    }

    static class Replies {
        public CommentRepliesRenderer commentRepliesRenderer;
    }

    static class CommentRepliesRenderer {
        public List<Continuation> continuations;
        public Text moreText;
//        public String trackingParams;
        public Text lessText;
    }

    static class Comment {
        public CommentRenderer commentRenderer;
    }
}
