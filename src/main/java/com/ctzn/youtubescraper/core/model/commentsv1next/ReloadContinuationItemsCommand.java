package com.ctzn.youtubescraper.core.model.commentsv1next;

import java.util.List;

public class ReloadContinuationItemsCommand {
    public String targetId;
    public List<ContinuationItem> continuationItems;
    public String slot;
}
