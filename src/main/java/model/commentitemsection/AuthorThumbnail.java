package model.commentitemsection;

import lombok.Data;

import java.util.List;

@Data
class AuthorThumbnail {
    public List<Thumbnail> thumbnails;
    public Accessibility accessibility;
}
