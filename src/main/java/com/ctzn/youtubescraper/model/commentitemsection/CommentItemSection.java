package com.ctzn.youtubescraper.model.commentitemsection;

import com.ctzn.youtubescraper.model.CommentThreadHeader;
import com.ctzn.youtubescraper.model.ContinuationData;
import lombok.Value;

import java.util.List;
import java.util.stream.Collectors;

@Value
public class CommentItemSection {
    public List<Content> contents;
    public List<Continuation> continuations;
    public String trackingParams;
    public Header header;
    public String sectionIdentifier;

    private static ContinuationData newContinuationData(NextContinuationData nextContinuationData) {
        return new ContinuationData(nextContinuationData.getContinuation(), nextContinuationData.getClickTrackingParams());
    }

    private String joinRuns(List<Run> runs) {
        return runs.stream().map(Run::getText).collect(Collectors.joining());
    }

    public int commentsCount() {
        return contents.size();
    }

    public boolean hasContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public ContinuationData nextContinuation() {
        if (!hasContinuation()) throw new IllegalStateException();
        return newContinuationData(continuations.get(0).getNextContinuationData());
    }

    public boolean hasHeader() {
        return header != null;
    }

    public CommentThreadHeader getHeader() {
        if (!hasHeader()) throw new IllegalStateException();
        CommentsHeaderRenderer headerContext = header.getCommentsHeaderRenderer();
        String commentsCountText = joinRuns(headerContext.getCountText().getRuns());
        List<ContinuationData> continuations = headerContext.sortMenu.sortFilterSubMenuRenderer.subMenuItems.stream()
                .map(i -> i.getContinuation().getReloadContinuationData()).map(CommentItemSection::newContinuationData).collect(Collectors.toList());
        return new CommentThreadHeader(continuations.get(0), continuations.get(1), commentsCountText);
    }

    public void printComments() {
        for (Content content : contents) {
            CommentThreadRenderer commentContext = content.getCommentThreadRenderer();
            CommentRenderer comment = commentContext.getComment().getCommentRenderer();
            System.out.println("================================================================================");
            System.out.println("CommentId:  " + comment.getCommentId());
            System.out.println(String.format("Author:     %s %s (%s)",
                    comment.getAuthorText().getSimpleText(),
                    comment.getAuthorEndpoint().getBrowseEndpoint().getCanonicalBaseUrl(),
                    joinRuns(comment.getPublishedTimeText().runs)));
            System.out.println("Text:       " + joinRuns(comment.getContentText().getRuns()));
            System.out.println("Like/Reply: " + comment.getLikeCount() + " / " + comment.getReplyCount());
        }
    }
}
