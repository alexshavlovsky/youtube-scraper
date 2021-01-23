package com.ctzn.youtubescraper.model.channelvideos;

import com.ctzn.youtubescraper.model.commons.NavigationEndpoint;
import com.ctzn.youtubescraper.model.commons.SimpleText;
import com.ctzn.youtubescraper.model.commons.Text;
import com.ctzn.youtubescraper.model.commons.Thumbnails;
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
