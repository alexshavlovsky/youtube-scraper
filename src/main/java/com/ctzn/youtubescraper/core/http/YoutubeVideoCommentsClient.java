package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.http.useragent.UserAgentFactory;
import com.ctzn.youtubescraper.core.model.browsev1.V1ApiRequest;
import com.ctzn.youtubescraper.core.model.comments.CommentItemSection;
import com.ctzn.youtubescraper.core.model.commentsv1next.NextApiResponse;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;
import com.ctzn.youtubescraper.core.parser.CommentApiResponseParser;
import com.ctzn.youtubescraper.core.parser.json.JsonMapper;
import lombok.extern.java.Log;

import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static com.ctzn.youtubescraper.core.http.IoUtil.*;

@Log
public class YoutubeVideoCommentsClient extends GenericYoutubeV1Client<CommentItemSection> {

    private static final CommentApiResponseParser commentApiResponseParser = new CommentApiResponseParser();

    private final String videoId;

    public YoutubeVideoCommentsClient(UserAgentFactory userAgentFactory, String videoId) throws ScraperParserException, ScraperHttpException, ScrapperInterruptedException {
        super(userAgentFactory, uriFactory.newVideoPageUri(videoId), videoPageBodyParser::scrapeInitialCommentItemSection);
        this.videoId = videoId;
        // TODO deal with new Youtube API changes
        // now initial comment section looks like this:
        // {"contents":[{"elementRenderer":{"trackingParams":"CJYBEO67BRgCIhMIpo6jsa3q7wIVGK9VCh37mwL6","newElement":{"type":{"componentType":{"templateConfig":{"uriTemplateConfig":{"uri":"comments_composite_entry_point.eml|b29e3310d2f51305"}},"model":{"commentsCompositeEntryPointModel":{"header":{"basicHeaderText":{"headerText":"Comments","commentCount":"9"},"trackingParams":"CJgBEMTgBCITCKaOo7Gt6u8CFRivVQod-5sC-g==","loggingDirectives":{"trackingParams":"CJgBEMTgBCITCKaOo7Gt6u8CFRivVQod-5sC-g==","visibility":{"types":"12"}}},"theme":{"colors":{"brandBackgroundSolid":4294967295,"brandBackgroundPrimary":4211081215,"brandBackgroundSecondary":4076863487,"backgroundA":4294243572,"iconInactive":4287664272,"iconDisabled":4291611852,"badgeChipBackground":218103808,"buttonChipBackgroundHover":436207616,"touchResponse":4278190080,"brandIconActive":4294901760,"brandIconInactive":4284506208,"brandButtonBackground":4291559424,"brandLinkText":4291559424,"tenPercentLayer":436207616,"snackbarBackground":4280821800,"themedBlue":4278607828,"themedGreen":4279268630,"staticBrandRed":4294901760,"staticBrandWhite":4294967295,"staticBrandBlack":4280821800,"staticClearColor":16777215,"staticAdYellow":4294688813,"staticGrey":4284506208,"overlayTextPrimary":4294967295,"overlayTextSecondary":3019898879,"separator":4292927712,"thumbnailOverlayIcon":3741319167,"selected":4293322470,"highlighted":218103808,"borderGrey":4293322470,"blackDim":2315255808,"videoProgressBarBackground":3019898879,"legacyBlue":4282549748,"iconActiveOther":4284506208,"errorBackground":4280361249,"suggestedAction":4294113535,"overlayButtonPrimary":1308622847,"overlayButtonSecondary":452984831,"callToAction":4278607828,"overlayBackgroundBrand":3855351808,"overlayBackgroundMediumLight":1291845632,"verifiedBadgeBackground":637534208,"staticClearBlack":0,"overlayBackgroundSolid":4278190080,"overlayBackgroundHeavy":3422552064,"adIndicator":4278219116,"textDisabled":4287664272,"textPrimaryInverse":4294967295,"overlayCallToAction":4282296063,"overlayBackgroundMedium":2566914048,"themedOverlayBackground":3019898879,"wordmarkText":4280821800,"brandBackgroundSolidUpdated":4294967295,"backgroundAUpdated":4294243572,"overlayBackgroundLight":436207616,"overlayTextDisabled":1308622847,"generalBackgroundC":4293059298,"generalBackgroundB":4293651435,"generalBackgroundA":4294243572,"textSecondary":4284506208,"textPrimary":4279440147},"fonts":{},"icons":{},"layout":{"spacing":{"space1":4,"space2":8,"space3":12,"space4":16,"space5":20,"space6":24,"space7":28,"space8":32,"space9":36,"space10":40,"space0":0},"fixedGrid":{"margin":16,"gutter":16},"icon":{"width":24,"height":24},"button":{"minWidthTextButton":88,"minWidthIconButton":56,"paddingX":16,"cornerRadius":2,"strokeThickness":1}},"themeType":"USER_INTERFACE_THEME_UNKNOWN"},"onTap":{"innertubeCommand":{"clickTrackingParams":"CJcBEMaJBRgAIhMIpo6jsa3q7wIVGK9VCh37mwL6","showEngagementPanelEndpoint":{"panelIdentifier":"comment-item-section"}}},"trackingParams":"CJcBEMaJBRgAIhMIpo6jsa3q7wIVGK9VCh37mwL6","environment":{},"loggingDirectives":{"trackingParams":"CJcBEMaJBRgAIhMIpo6jsa3q7wIVGK9VCh37mwL6","visibility":{"types":"12"}}}}}},"properties":{"identifierProperties":{"identifier":"comments_composite_entry_point.eml|b29e3310d2f51305","uniqueLoggingIdentifier":"1617737384768589466"}}}}}],"trackingParams":"CJYBEO67BRgCIhMIpo6jsa3q7wIVGK9VCh37mwL6","sectionIdentifier":"comments-entry-point"}
        // a further investigation is needed
        if (!initialData.hasContinuation()) throw new ScraperParserException("Initial comment continuation not found");
    }

    public NextApiResponse requestNextCommentSection(NextContinuationData continuationData, RequestUriLengthLimiter limiter) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        return requestNextSection(continuationData);
    }

    public NextApiResponse requestNextReplySection(NextContinuationData continuationData, RequestUriLengthLimiter limiter) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {
        return requestNextSection(continuationData);
    }

    private NextApiResponse requestNextSection(NextContinuationData continuationData) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException {

        URI requestUri = uriFactory.newNextApiV1RequestUri(youtubeCfg);

        V1ApiRequest requestModel = new V1ApiRequest(clientCtx, continuationData.getContinuation());
        String requestBody = JsonMapper.asJson(requestModel);

        HttpRequest request = newV1ApiRequestBuilder(requestUri)
                .headers("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody)).build();

        HttpResponse<InputStream> response = completeRequest(httpClient, request);

        String body = readStreamToString(applyBrotliDecoderAndGetBody(response));

        NextApiResponse nextApiResponse = JsonMapper.parse(body, NextApiResponse.class);

        return nextApiResponse;
    }

    public String getVideoId() {
        return videoId;
    }

    public NextContinuationData getInitialCommentSectionContinuation() {
        return initialData.getContinuation();
    }
}
