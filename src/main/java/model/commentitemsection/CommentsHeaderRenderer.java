package model.commentitemsection;

import lombok.Data;

@Data
class CommentsHeaderRenderer {
    public CountText countText;
    public CreateRenderer createRenderer;
    public SortMenu sortMenu;
    public String trackingParams;
    public TitleText titleText;
    public CommentsCount commentsCount;
    public boolean showSeparator;
    public LoggingDirectives loggingDirectives;
}
