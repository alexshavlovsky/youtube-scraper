package model.commentitemsection;

import lombok.Data;

import java.util.List;

@Data
class SortFilterSubMenuRenderer {
    public List<SubMenuItem> subMenuItems;
    public String title;
    public Icon icon;
    public Accessibility accessibility;
    public String tooltip;
    public String trackingParams;
}
