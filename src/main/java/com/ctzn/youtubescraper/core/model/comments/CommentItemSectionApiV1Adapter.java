package com.ctzn.youtubescraper.core.model.comments;

import com.ctzn.youtubescraper.core.model.commentsv1next.*;
import com.ctzn.youtubescraper.core.model.commons.ContinuationEndpoint;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.parser.ParserUtil;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;

import java.util.*;

import static java.util.Objects.nonNull;

// TODO replace the CommentItemSection class
public class CommentItemSectionApiV1Adapter extends CommentItemSection {

    private final CommentsHeaderRenderer header;

    private final List<CommentThreadRenderer> comments;

    private final ContinuationEndpoint continuation;

    public CommentItemSectionApiV1Adapter(NextApiResponse nextApiResponse) {
        this.header = nextApiResponse.getHeader();
        this.comments = nextApiResponse.getComments();
        this.continuation = nextApiResponse.getContinuation();
    }

    @Override
    public boolean hasHeader() {
        return header != null;
    }

    @Override
    public boolean hasContent() {
        return comments != null && !comments.isEmpty();
    }

    @Override
    public boolean hasContinuation() {
        return continuation != null;
    }

    @Override
    public boolean hasDeprecatedContinuation() {
        return false;
    }

    @Override
    public boolean hasReplies() {
        return sumReplyCounters() > 0;
    }

    @Override
    public int countContentPieces() {
        return hasContent() ? comments.size() : 0;
    }

    @Override
    public NextContinuationData getContinuation() {
        return continuation.asContinuation();
    }

    @Override
    public SectionHeaderDTO getHeader() {
        return header.getValue();
    }

    @Override
    public int sumReplyCounters() {
        if (!hasContent()) return 0;
        return comments.stream().mapToInt(c -> c.comment.commentRenderer.replyCount).sum();
    }

    @Override
    public int countReplyContinuations() {
        if (!hasContent()) return 0;
        return getReplyContinuationMap().size();
    }

    @Override
    public Map<String, NextContinuationData> getReplyContinuationMap() {
        if (!hasContent()) return Collections.emptyMap();
        Map<String, NextContinuationData> map = new HashMap<>();
        for (CommentThreadRenderer ctr : comments) {
            if (nonNull(ctr.replies) &&
                nonNull(ctr.replies.commentRepliesRenderer) &&
                nonNull(ctr.replies.commentRepliesRenderer.contents) &&
                !ctr.replies.commentRepliesRenderer.contents.isEmpty()) {
                List<Content> content = ctr.replies.commentRepliesRenderer.contents;
                content.stream()
                        .filter(e -> nonNull(e.continuationItemRenderer))
                        .map(e -> e.continuationItemRenderer.continuationEndpoint.asContinuation())
                        .forEach(c -> map.put(ctr.comment.commentRenderer.commentId, c));
            }
        }
        return map;
    }

    @Override
    public List<CommentDTO> getComments(String videoId, String parentCommentId) {
        if (!hasContent()) return Collections.emptyList();
        List<CommentDTO> list = new ArrayList<>();
        for (CommentThreadRenderer c : comments) {
            CommentRenderer r = c.comment.commentRenderer;
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
