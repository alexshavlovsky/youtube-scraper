package com.ctzn.youtubescraper.core.model.channelmetadata;

import com.ctzn.youtubescraper.core.model.commons.Thumbnail;
import lombok.Value;

import java.util.List;

@Value
public class ChannelMicroformatDTO {
    String urlCanonical;
    String title;
    String description;
    Thumbnail thumbnail;
    String siteName;
    String appName;
    String androidPackage;
    String iosAppStoreId;
    String iosAppArguments;
    String ogType;
    String urlApplinksWeb;
    String urlApplinksIos;
    String urlApplinksAndroid;
    String urlTwitterIos;
    String urlTwitterAndroid;
    String twitterCardType;
    String twitterSiteHandle;
    String schemaDotOrgType;
    boolean noindex;
    boolean unlisted;
    boolean familySafe;
    List<String> tags;
    List<String> availableCountries;
    List<LinkAlternate> linkAlternates;
}
