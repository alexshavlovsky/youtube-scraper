package com.ctzn.youtubescraper.core.model.channelmetadata;

import com.ctzn.youtubescraper.core.model.commons.Thumbnails;
import lombok.Value;

import java.util.List;

@Value
public class ChannelMetadataDTO {
    String title;
    String description;
    String rssUrl;
    String externalId;
    String keywords;
    List<String> ownerUrls;
    Thumbnails avatar;
    String channelUrl;
    boolean isFamilySafe;
    List<String> availableCountryCodes;
    String androidDeepLink;
    String androidAppindexingLink;
    String iosAppindexingLink;
    String tabPath;
    String vanityChannelUrl;
}
