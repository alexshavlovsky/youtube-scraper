package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class ButtonRenderer {
    public String style;
    public String size;
    public Text text;
    public NavigationEndpoint navigationEndpoint;
    public String trackingParams;
    public Icon icon;
    public String iconPosition;
}
