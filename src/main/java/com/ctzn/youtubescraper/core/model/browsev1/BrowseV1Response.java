package com.ctzn.youtubescraper.core.model.browsev1;

import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.model.channelmetadata.Metadata;
import com.ctzn.youtubescraper.core.model.channelmetadata.Microformat;
import com.ctzn.youtubescraper.core.model.channelvideos.Item;
import com.ctzn.youtubescraper.core.model.channelvideos.VideosGrid;

import java.util.List;

public class BrowseV1Response {
    public Metadata metadata;
    public Microformat microformat;
    public List<OnResponseReceivedAction> onResponseReceivedActions;

    public VideosGrid getVideosGrid() throws ScraperParserException {
        if (onResponseReceivedActions != null && !onResponseReceivedActions.isEmpty()) {
            OnResponseReceivedAction action = onResponseReceivedActions.get(0);
            if (action.appendContinuationItemsAction != null &&
                    action.appendContinuationItemsAction.continuationItems != null) {
                return new VideosGrid(action.appendContinuationItemsAction.continuationItems, null);
            }
        }
        throw new ScraperParserException("Unable to parse browse API v1 response");
    }

    static class OnResponseReceivedAction {
        public VideosGridContinuation appendContinuationItemsAction;
    }

    static class VideosGridContinuation {
        public List<Item> continuationItems;
    }
}
