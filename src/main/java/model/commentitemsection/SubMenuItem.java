package model.commentitemsection;

import lombok.Data;

@Data
class SubMenuItem {
    public String title;
    public boolean selected;
    public ReloadContinuation continuation;
}
