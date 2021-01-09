package model.commentitemsection;

import lombok.Data;

@Data
class AuthorCommentBadgeRenderer {
    public Icon icon;
    public AuthorText authorText;
    public AuthorEndpoint authorEndpoint;
    public String iconTooltip;
}
