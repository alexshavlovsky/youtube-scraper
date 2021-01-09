package model.commentitemsection;

import lombok.Data;

@Data
class ButtonRenderer {
    public String style;
    public String size;
    public Text text;
    public NavigationEndpoint navigationEndpoint;
    public String trackingParams;
    public Accessibility accessibility;
    public Icon icon;
    public String iconPosition;
}
