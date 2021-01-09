package model.commentitemsection;

import lombok.Data;

@Data
class ToggleButtonRenderer {
    public Style style;
    public Size size;
    public boolean isToggled;
    public boolean isDisabled;
    public DefaultIcon defaultIcon;
    public String trackingParams;
    public String defaultTooltip;
    public String toggledTooltip;
    public ToggledStyle toggledStyle;
    public DefaultNavigationEndpoint defaultNavigationEndpoint;
    public AccessibilityData accessibilityData;
    public ToggledAccessibilityData toggledAccessibilityData;
}
