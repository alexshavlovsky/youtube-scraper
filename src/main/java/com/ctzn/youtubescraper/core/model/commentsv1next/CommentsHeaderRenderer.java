package com.ctzn.youtubescraper.core.model.commentsv1next;

import com.ctzn.youtubescraper.core.model.comments.SectionHeaderDTO;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.model.commons.Text;

import java.util.List;
import java.util.stream.Collectors;

public class CommentsHeaderRenderer {
    public Text countText;
    public CreateRenderer createRenderer;
    public SortMenu sortMenu;
    public String trackingParams;
    public Text titleText;
    public Text commentsCount;
    public boolean showSeparator;

    private List<NextContinuationData> getSortMenuReloadContinuations() {
        return sortMenu.sortFilterSubMenuRenderer.subMenuItems.stream()
                .map(i -> i.serviceEndpoint.asContinuation()).collect(Collectors.toList());
    }

    public SectionHeaderDTO getValue() {
        List<NextContinuationData> continuations = getSortMenuReloadContinuations();
        return new SectionHeaderDTO(continuations.get(0), continuations.get(1), countText.toString());
    }

}
