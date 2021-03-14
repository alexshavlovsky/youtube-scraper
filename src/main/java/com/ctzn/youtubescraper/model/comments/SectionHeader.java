package com.ctzn.youtubescraper.model.comments;

import com.ctzn.youtubescraper.model.commons.*;

import java.util.List;
import java.util.stream.Collectors;

public class SectionHeader {
    public CommentsHeaderRenderer commentsHeaderRenderer;

    private List<NextContinuationData> getSortMenuReloadContinuations() {
        return commentsHeaderRenderer.sortMenu.sortFilterSubMenuRenderer.subMenuItems.stream()
                .map(i -> i.continuation.reloadContinuationData).collect(Collectors.toList());
    }

    private String getCommentsCountText() {
        return commentsHeaderRenderer.countText.toString();
    }

    public SectionHeaderDTO getValue() {
        List<NextContinuationData> continuations = getSortMenuReloadContinuations();
        return new SectionHeaderDTO(continuations.get(0), continuations.get(1), getCommentsCountText());
    }

    static class CommentsHeaderRenderer {
        public Text countText;
        public CreateRenderer createRenderer;
        public SortMenu sortMenu;
//        public String trackingParams;
        public Text titleText;
        public SimpleText commentsCount;
        public boolean showSeparator;
    }

    static class CreateRenderer {
        public CommentSimpleboxRenderer commentSimpleboxRenderer;
    }

    static class CommentSimpleboxRenderer {
        public Thumbnails authorThumbnail;
        public Text placeholderText;
        public PrepareAccountEndpoint prepareAccountEndpoint;
//        public String trackingParams;
        public String avatarSize;
    }

    static class PrepareAccountEndpoint {
        public String clickTrackingParams;
        public CommandMetadata commandMetadata;
    }

    static class SortFilterSubMenuRenderer {
        public List<SubMenuItem> subMenuItems;
        public String title;
        public String tooltip;
//        public String trackingParams;
    }

    static class SortMenu {
        public SortFilterSubMenuRenderer sortFilterSubMenuRenderer;
    }

    static class SubMenuItem {
        public String title;
        public boolean selected;
        public ReloadContinuation continuation;
    }

    static class ReloadContinuation {
        public NextContinuationData reloadContinuationData;
    }
}
