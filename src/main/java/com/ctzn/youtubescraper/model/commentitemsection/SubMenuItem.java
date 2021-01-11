package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class SubMenuItem {
    public String title;
    public boolean selected;
    public ReloadContinuation continuation;
}
