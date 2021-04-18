package com.ctzn.youtubescraper.core.model.channelvideos;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.model.commons.Continuation;
import com.ctzn.youtubescraper.core.model.commons.ContinuationEndpoint;
import com.ctzn.youtubescraper.core.model.commons.ContinuationItemRenderer;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.parser.ParserUtil;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
import lombok.Value;
import lombok.extern.java.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ctzn.youtubescraper.core.parser.ParserUtil.parseDigitsToInt;

@Log
@Value
public class VideosGrid {
    public List<Item> items;
    public List<Continuation> continuations;

    public boolean hasContent() {
        return items != null && !items.isEmpty();
    }

    public int countContentPieces() {
        return hasContent() ? (int) items.stream().filter(item -> item.gridVideoRenderer != null).count() : 0;
    }

    public boolean hasContinuation() {
        return hasDeprecatedContinuation() || hasContinuationItemTrigger();
    }

    private boolean hasDeprecatedContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    private static boolean hasContinuationTrigger(Item item) {
        ContinuationItemRenderer renderer = item.continuationItemRenderer;
        return (renderer != null && "CONTINUATION_TRIGGER_ON_ITEM_SHOWN".equals(renderer.trigger));
    }

    private boolean hasContinuationItemTrigger() {
        if (!hasContent()) return false;
        Item item = items.get(items.size() - 1);
        if (!hasContinuationTrigger(item)) return false;
        ContinuationEndpoint endpoint = item.continuationItemRenderer.continuationEndpoint;
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

    public NextContinuationData getContinuation() {
        if (!hasContinuation()) throw new IllegalStateException("The grid hasn't continuation data");
        if (hasDeprecatedContinuation()) return continuations.get(0).nextContinuationData;
        return items.get(items.size() - 1).continuationItemRenderer.continuationEndpoint.asContinuation();
    }

    public List<VideoDTO> getVideos(String channelId) {
        if (!hasContent()) return Collections.emptyList();
        List<VideoDTO> list = new ArrayList<>();
        for (Item item : items) {
            if (hasContinuationTrigger(item)) continue;
            GridVideoRenderer r = item.gridVideoRenderer;
            if (r == null) {
                log.warning("An unknown element occurred in a video grid");
                continue;
            }
            String publishedTimeText = r.getPublishedTimeText() == null ? null : r.getPublishedTimeText().toString();
            VideoDTO videoDTO = new VideoDTO(
                    channelId,
                    r.getVideoId(),
                    r.getTitle() == null ? null : r.getTitle().toString(),
                    publishedTimeText, // if null then this video is probably a live stream
                    ParserUtil.parsePublishedTimeText(publishedTimeText),
                    r.getViewCountText() == null ? 0 : parseDigitsToInt(r.getViewCountText().toString())
            );
            list.add(videoDTO);
        }
        return list;
    }
}
