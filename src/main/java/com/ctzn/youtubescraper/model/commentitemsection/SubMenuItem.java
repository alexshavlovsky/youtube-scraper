package com.ctzn.youtubescraper.model.commentitemsection;

import com.ctzn.youtubescraper.model.continuation.ReloadContinuation;
import lombok.Value;

@Value
class SubMenuItem {
    public String title;
    public boolean selected;
    public ReloadContinuation continuation;
}
