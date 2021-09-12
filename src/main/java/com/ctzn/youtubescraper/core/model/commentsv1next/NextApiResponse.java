package com.ctzn.youtubescraper.core.model.commentsv1next;

import com.ctzn.youtubescraper.core.model.commons.ContinuationEndpoint;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;

public class NextApiResponse {

    public List<OnResponseReceivedEndpoint> onResponseReceivedEndpoints;

    public CommentsHeaderRenderer getHeader() {
        return onResponseReceivedEndpoints.stream()
                .filter(e -> nonNull(e.reloadContinuationItemsCommand))
                .filter(e -> "RELOAD_CONTINUATION_SLOT_HEADER".equals(e.reloadContinuationItemsCommand.slot))
                .findAny()
                .map(e -> e.reloadContinuationItemsCommand.continuationItems.get(0).commentsHeaderRenderer)
                .orElse(null);
    }

    public List<CommentThreadRenderer> getComments() {
        return onResponseReceivedEndpoints.stream()
                .filter(e -> nonNull(e.reloadContinuationItemsCommand))
                .filter(e -> "RELOAD_CONTINUATION_SLOT_BODY".equals(e.reloadContinuationItemsCommand.slot))
                .findAny()
                .filter(e -> nonNull(e.reloadContinuationItemsCommand.continuationItems))
                .map(e -> e.reloadContinuationItemsCommand.continuationItems.stream()
                        .filter(i -> nonNull(i.commentThreadRenderer))
                        .map(i -> i.commentThreadRenderer)
                        .collect(Collectors.toList()))
                .orElse(
                        onResponseReceivedEndpoints.stream()
                                .filter(e -> nonNull(e.appendContinuationItemsAction))
                                .filter(e -> "comments-section".equals(e.appendContinuationItemsAction.targetId))
                                .findAny()
                                .filter(e -> nonNull(e.appendContinuationItemsAction.continuationItems))
                                .map(e -> e.appendContinuationItemsAction.continuationItems.stream()
                                        .filter(i -> nonNull(i.commentThreadRenderer))
                                        .map(i -> i.commentThreadRenderer)
                                        .collect(Collectors.toList()))
                                .orElse(
                                        onResponseReceivedEndpoints.stream()
                                                .filter(e -> nonNull(e.appendContinuationItemsAction))
                                                .filter(e -> nonNull(e.appendContinuationItemsAction.targetId))
                                                .filter(e -> e.appendContinuationItemsAction.targetId.startsWith("comment-replies-item"))
                                                .findAny()
                                                .filter(e -> nonNull(e.appendContinuationItemsAction.continuationItems))
                                                .map(e -> e.appendContinuationItemsAction.continuationItems.stream()
                                                        .filter(i -> nonNull(i.commentRenderer))
                                                        .map(i -> CommentThreadRenderer.fromCommentRenderer(i.commentRenderer))
                                                        .collect(Collectors.toList()))
                                                .orElse(null)
                                )
                );
    }

    public ContinuationEndpoint getContinuation() {
        return onResponseReceivedEndpoints.stream()
                .filter(e -> nonNull(e.reloadContinuationItemsCommand))
                .filter(e -> "RELOAD_CONTINUATION_SLOT_BODY".equals(e.reloadContinuationItemsCommand.slot))
                .findAny()
                .filter(e -> nonNull(e.reloadContinuationItemsCommand.continuationItems))
                .flatMap(e -> e.reloadContinuationItemsCommand.continuationItems.stream()
                        .filter(i -> nonNull(i.continuationItemRenderer))
                        .map(i -> i.continuationItemRenderer.continuationEndpoint).findAny())
                .orElse(
                        onResponseReceivedEndpoints.stream()
                                .filter(e -> nonNull(e.appendContinuationItemsAction))
                                .filter(e -> "comments-section".equals(e.appendContinuationItemsAction.targetId))
                                .findAny()
                                .filter(e -> nonNull(e.appendContinuationItemsAction.continuationItems))
                                .flatMap(e -> e.appendContinuationItemsAction.continuationItems.stream()
                                        .filter(i -> nonNull(i.continuationItemRenderer))
                                        .map(i -> i.continuationItemRenderer.continuationEndpoint).findAny())
                                .orElse(
                                        onResponseReceivedEndpoints.stream()
                                                .filter(e -> nonNull(e.appendContinuationItemsAction))
                                                .filter(e -> nonNull(e.appendContinuationItemsAction.targetId))
                                                .filter(e -> e.appendContinuationItemsAction.targetId.startsWith("comment-replies-item"))
                                                .findAny()
                                                .filter(e -> nonNull(e.appendContinuationItemsAction.continuationItems))
                                                .flatMap(e -> e.appendContinuationItemsAction.continuationItems.stream()
                                                        .filter(i -> nonNull(i.continuationItemRenderer))
                                                        .filter(i -> nonNull(i.continuationItemRenderer.button))
                                                        .filter(i -> nonNull(i.continuationItemRenderer.button.buttonRenderer))
                                                        .map(i -> i.continuationItemRenderer.button.buttonRenderer.command)
                                                        .findAny())
                                                .orElse(null)
                                )
                );
    }

}
