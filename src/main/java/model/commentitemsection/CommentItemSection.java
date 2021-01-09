package model.commentitemsection;

import lombok.Data;
import model.ContinuationData;

import java.util.List;

@Data
public class CommentItemSection {
    public List<Content> contents;
    public List<Continuation> continuations;
    public String trackingParams;
    public Header header;
    public String sectionIdentifier;

    public boolean hasContinuation() {
        return !continuations.isEmpty();
    }

    public ContinuationData getContinuationData() {
        NextContinuationData nextContinuationData = continuations.get(0).nextContinuationData;
        return new ContinuationData(nextContinuationData.continuation, nextContinuationData.clickTrackingParams);
    }

    public void printComments() {
        for (Content content : contents) {
            CommentRenderer comment = content.commentThreadRenderer.comment.commentRenderer;
            System.out.println(comment);
        }
    }
}
