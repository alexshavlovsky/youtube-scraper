package com.ctzn.youtubescraper.core.model.channelvideos;

import com.ctzn.youtubescraper.core.model.commons.NavigationEndpoint;
import com.ctzn.youtubescraper.core.model.commons.SimpleText;
import com.ctzn.youtubescraper.core.model.commons.Text;
import com.ctzn.youtubescraper.core.model.commons.Thumbnails;
import lombok.Value;

@Value
public class GridVideoRenderer {
    String videoId;
    Thumbnails thumbnail;
    Text title;
    SimpleText publishedTimeText;
    SimpleText viewCountText;
    NavigationEndpoint navigationEndpoint;
    SimpleText shortViewCountText;
    RichThumbnail richThumbnail;
}
