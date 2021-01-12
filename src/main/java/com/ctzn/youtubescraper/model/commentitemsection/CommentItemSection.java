package com.ctzn.youtubescraper.model.commentitemsection;

import com.ctzn.youtubescraper.model.CommentThreadHeader;
import com.ctzn.youtubescraper.model.ContinuationData;
import lombok.Value;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Value
public class CommentItemSection {
    public List<Content> contents;
    public List<Continuation> continuations;
    public String trackingParams;
    public Header header;
    public String sectionIdentifier;

    private static ContinuationData newContinuationData(NextContinuationData nextContinuationData) {
        return new ContinuationData(nextContinuationData.continuation, nextContinuationData.clickTrackingParams);
    }

    private String joinRuns(List<Run> runs) {
        return runs.stream().map(r -> r.text).collect(Collectors.joining());
    }

    public boolean hasHeader() {
        return header != null;
    }

    public boolean hasComments() {
        return contents != null && !contents.isEmpty();
    }

    public boolean hasContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public boolean hasReplies() {
        return sumReplyCounters() > 0;
    }

    public int countComments() {
        return hasComments() ? contents.size() : 0;
    }

    public ContinuationData nextContinuation() {
        if (!hasContinuation()) throw new IllegalStateException();
        return newContinuationData(continuations.get(0).nextContinuationData);
    }

    public CommentThreadHeader getHeader() {
        if (!hasHeader()) throw new IllegalStateException();
        CommentsHeaderRenderer headerContext = header.commentsHeaderRenderer;
        String commentsCountText = joinRuns(headerContext.countText.runs);
        List<ContinuationData> continuations = headerContext.sortMenu.sortFilterSubMenuRenderer.subMenuItems.stream()
                .map(i -> i.continuation.reloadContinuationData).map(CommentItemSection::newContinuationData).collect(Collectors.toList());
        return new CommentThreadHeader(continuations.get(0), continuations.get(1), commentsCountText);
    }

    public int sumReplyCounters() {
        if (!hasComments()) return 0;
        return contents.stream().mapToInt(c -> c.commentThreadRenderer.comment.commentRenderer.replyCount).sum();
    }

    public int countReplyContinuations() {
        if (!hasComments()) return 0;
        return contents.stream().map(c -> c.commentThreadRenderer)
                .mapToInt(c -> c.replies == null ? 0 : c.replies.commentRepliesRenderer.continuations.size()).sum();
    }

    public Map<String, ContinuationData> getReplyContinuationsMap() {
        if (!hasComments()) return Collections.emptyMap();
        Map<String, ContinuationData> map = new LinkedHashMap<>();
        for (Content content : contents) {
            CommentThreadRenderer commentContext = content.commentThreadRenderer;
            if (commentContext.replies != null) {
                CommentRenderer comment = commentContext.comment.commentRenderer;
                CommentRepliesRenderer replies = commentContext.replies.commentRepliesRenderer;
                map.put(comment.commentId,
                        newContinuationData(replies.continuations.get(0).nextContinuationData));
            }
        }
        return map;
    }

    public void printComments() {
        if (!hasComments()) return;
        for (Content content : contents) {
            CommentThreadRenderer commentContext = content.commentThreadRenderer;
            CommentRenderer comment = commentContext.comment.commentRenderer;
            System.out.println("================================================================================");
            System.out.println("CommentId:  " + comment.commentId);
            System.out.println(String.format("Author:     %s %s (%s)",
                    comment.authorText.simpleText,
                    comment.authorEndpoint.browseEndpoint.canonicalBaseUrl,
                    joinRuns(comment.publishedTimeText.runs)));
            System.out.println("Text:       " + joinRuns(comment.contentText.runs));
            System.out.println("Like/Reply: " + comment.likeCount + " / " + comment.replyCount);
        }
    }
}
