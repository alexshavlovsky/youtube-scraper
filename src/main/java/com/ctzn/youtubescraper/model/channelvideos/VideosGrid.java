package com.ctzn.youtubescraper.model.channelvideos;

import com.ctzn.youtubescraper.model.VideoDTO;
import com.ctzn.youtubescraper.model.commons.Continuation;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.ctzn.youtubescraper.parser.ParserUtil.parseDigitsToInt;

public class VideosGrid {
    public List<Item> items;
    public List<Continuation> continuations;

    public boolean hasContent() {
        return items != null && !items.isEmpty();
    }

    public int countContentPieces() {
        return hasContent() ? items.size() : 0;
    }

    public boolean hasContinuation() {
        return continuations != null && !continuations.isEmpty();
    }

    public NextContinuationData getContinuation() {
        if (!hasContinuation()) throw new IllegalStateException("The grid hasn't continuation data");
        return continuations.get(0).nextContinuationData;
    }

    public List<VideoDTO> getVideos(String channelId) {
        if (!hasContent()) return Collections.emptyList();
        List<VideoDTO> list = new ArrayList<>();
        for (Item item : items) {
            GridVideoRenderer r = item.gridVideoRenderer;
            VideoDTO videoDTO = new VideoDTO(
                    channelId,
                    r.getVideoId(),
                    r.getTitle().toString(),
                    r.getPublishedTimeText() == null ? null : r.getPublishedTimeText().toString(), // if null then this video is probably a live stream
                    parseDigitsToInt(r.getViewCountText().toString())
            );
            list.add(videoDTO);
        }
        return list;
    }
}
