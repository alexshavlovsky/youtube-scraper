package com.ctzn.youtubescraper.core.model.comments;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.model.commentsv1next.*;
import com.ctzn.youtubescraper.core.model.commons.Continuation;
import com.ctzn.youtubescraper.core.model.commons.ContinuationEndpoint;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.parser.ParserUtil;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import lombok.extern.java.Log;

import java.util.*;

@Log
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
        return hasDeprecatedContinuation() || hasContinuationItemTrigger();
    }

    public boolean hasDeprecatedContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public boolean hasReplies() {
        return sumReplyCounters() > 0;
    }

    public int countContentPieces() {
        return hasContent() ? contents.size() : 0;
    }

    public NextContinuationData getContinuation() {
        if (!hasContinuation()) throw new IllegalStateException("The grid hasn't continuation data");
        if (hasDeprecatedContinuation()) return continuations.get(0).nextContinuationData;
        return contents.get(contents.size() - 1).continuationItemRenderer.continuationEndpoint.asContinuation();
    }

    private static boolean hasContinuationTrigger(Content content) {
        ContinuationItemRenderer renderer = content.continuationItemRenderer;
        return (renderer != null && "CONTINUATION_TRIGGER_ON_ITEM_SHOWN".equals(renderer.trigger));
    }

    // TODO extract this duplicated code fragment (duplicate is located in the VideosGrid)
    private boolean hasContinuationItemTrigger() {
        if (!hasContent()) return false;
        Content content = contents.get(contents.size() - 1);
        if (!hasContinuationTrigger(content)) return false;
        ContinuationEndpoint endpoint = content.continuationItemRenderer.continuationEndpoint;
        try {
            ParserUtil.assertNotNull("", endpoint,
                    endpoint.clickTrackingParams,
                    endpoint.continuationCommand,
                    endpoint.continuationCommand.token
            );
        } catch (ScraperParserException e) {
            log.warning("Invalid CONTINUATION_TRIGGER_ON_ITEM_SHOWN occurred");
            return false;
        }
        return true;
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
        return 0;
//        return contents.stream().map(c -> c.commentThreadRenderer)
//                .mapToInt(c -> c.replies == null ? 0 : c.replies.commentRepliesRenderer.continuations.size()).sum();
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
//                map.put(comment.commentId, replies.continuations.get(0).nextContinuationData);
            }
        }
        return map;
    }

    public List<CommentDTO> getComments(String videoId, String parentCommentId) {
        if (!hasContent()) return Collections.emptyList();
        List<CommentDTO> list = new ArrayList<>();
        for (Content c : contents) {
            CommentRenderer r = c.getCommentRenderer();
            String publishedTimeText = r.publishedTimeText == null ? null : r.publishedTimeText.toString();
            CommentDTO commentDTO = new CommentDTO(
                    videoId,
                    r.commentId,
                    r.authorText == null ? "" : r.authorText.toString(),
                    r.authorEndpoint == null ? "" : r.authorEndpoint.browseEndpoint.browseId,
                    publishedTimeText,
                    ParserUtil.parsePublishedTimeText(publishedTimeText),
                    r.contentText.toString(),
                    r.voteCount == null ? 0 : (int) ParserUtil.parseSubCount(r.voteCount.toString()),
                    r.replyCount,
                    parentCommentId
            );
            list.add(commentDTO);
        }
        return list;
    }

}
