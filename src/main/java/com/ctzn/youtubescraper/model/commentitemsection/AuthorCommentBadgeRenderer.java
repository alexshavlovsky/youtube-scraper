package com.ctzn.youtubescraper.model.commentitemsection;

import lombok.Value;

@Value
class AuthorCommentBadgeRenderer {
    public Icon icon;
    public AuthorText authorText;
    public AuthorEndpoint authorEndpoint;
    public String iconTooltip;
}
