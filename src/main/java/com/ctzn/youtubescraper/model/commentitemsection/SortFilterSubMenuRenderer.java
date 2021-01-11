package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

import java.util.List;

@Value
class SortFilterSubMenuRenderer {
    public List<SubMenuItem> subMenuItems;
    public String title;
    public Icon icon;
    public Accessibility accessibility;
    public String tooltip;
    public String trackingParams;
}
