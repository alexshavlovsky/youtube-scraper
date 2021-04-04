package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.model.browsev1.ClientContext;
import com.ctzn.youtubescraper.core.model.browsev1.MainAppWebInfo;
import lombok.extern.java.Log;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.ArrayList;

@Log
abstract class AbstractYoutubeBrowseClient<E> extends AbstractYoutubeClient<E> {

    final ClientContext clientCtx;

    AbstractYoutubeBrowseClient(UserAgentFactory userAgentFactory, String pageUri, YoutubeInitialDataHandler<E> youtubeInitialDataHandler) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        super(userAgentFactory, pageUri, youtubeInitialDataHandler);
        clientCtx = videoPageBodyParser.parseClientContext(youtubeCfgJson);
        clientCtx.client.screenWidthPoints = 1536;
        clientCtx.client.screenHeightPoints = 768;
        clientCtx.client.screenPixelDensity = 1;
        clientCtx.client.screenDensityFloat = 1.25;
        clientCtx.client.utcOffsetMinutes = 180;
        clientCtx.client.userInterfaceTheme = "USER_INTERFACE_THEME_LIGHT";
        clientCtx.client.mainAppWebInfo = new MainAppWebInfo();
        clientCtx.client.mainAppWebInfo.graftUrl = pageUri;
        clientCtx.client.timeZone = "Europe/Minsk";
        // clientCtx.clientScreenNonce = ???
        clientCtx.request.consistencyTokenJars = new ArrayList<>();
        clientCtx.request.internalExperimentFlags = new ArrayList<>();
        //clientCtx.adSignalsInfo = ???
        cookies.put("PREF=f4=4000000&tz=Europe.Minsk");
    }

    HttpRequest.Builder newBrowseApiV1RequestBuilder(URI requestUri) {
        return newRequestBuilder(requestUri, "*/*")
                .headers("Referer", pageUri)
                .headers("Origin", "https://www.youtube.com")
                .headers("X-YouTube-Client-Name", youtubeCfg.getClientName())
                .headers("X-YouTube-Client-Version", youtubeCfg.getClientVersion())
                .header("X-Goog-Visitor-Id", youtubeCfg.getVisitorData())
                .headers("Cookie", cookies.getHeader());
    }
}
