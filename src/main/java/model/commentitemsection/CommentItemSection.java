package model.commentitemsection;

import lombok.Data;
import model.ContinuationData;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class CommentItemSection {
    public List<Content> contents;
    public List<Continuation> continuations;
    public String trackingParams;
    public Header header;
    public String sectionIdentifier;

    public boolean hasContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public ContinuationData nextContinuation() {
        if (!hasContinuation()) throw new IllegalStateException();
        return newContinuationData(continuations.get(0).getNextContinuationData());
    }

    private static ContinuationData newContinuationData(NextContinuationData nextContinuationData) {
        return new ContinuationData(nextContinuationData.getContinuation(), nextContinuationData.getClickTrackingParams());
    }

    private String joinRuns(List<Run> runs) {
        return runs.stream().map(Run::getText).collect(Collectors.joining());
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
