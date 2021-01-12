package com.ctzn.youtubescraper.model.replyapiresponse;

import com.ctzn.youtubescraper.model.continuation.Continuation;

import java.util.List;

class AuthorText {
    public String simpleText;
}

class Thumbnail {
    public String url;
    public int width;
    public int height;
}

class AuthorThumbnail {
    public List<Thumbnail> thumbnails;
}

class WebCommandMetadata {
    public String url;
    public String webPageType;
    public int rootVe;
    public String apiUrl;
}

class CommandMetadata {
    public WebCommandMetadata webCommandMetadata;
}

class BrowseEndpoint {
    public String browseId;
    public String canonicalBaseUrl;
}

class AuthorEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public BrowseEndpoint browseEndpoint;
}

class UrlEndpoint {
    public String url;
    public String target;
    public boolean nofollow;
}

class NavigationEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public UrlEndpoint urlEndpoint;
    public WatchEndpoint watchEndpoint;
    public SignInEndpoint signInEndpoint;
}

class Run {
    public String text;
    public NavigationEndpoint navigationEndpoint;
}

class ContentText {
    public List<Run> runs;
}

class WatchEndpoint {
    public String videoId;
    public String params;
}

class PublishedTimeText {
    public List<Run> runs;
}

class NextEndpoint {
    public String clickTrackingParams;
    public CommandMetadata commandMetadata;
    public WatchEndpoint watchEndpoint;
}

class SignInEndpoint {
    public NextEndpoint nextEndpoint;
}

class VoteCount {
    public String simpleText;
}

class Visibility {
    public String types;
}

class LoggingDirectives {
    public String trackingParams;
    public Visibility visibility;
}

class Icon {
    public String iconType;
}

class AuthorCommentBadgeRenderer {
    public Icon icon;
    public AuthorText authorText;
    public AuthorEndpoint authorEndpoint;
    public String iconTooltip;
}

class AuthorCommentBadge {
    public AuthorCommentBadgeRenderer authorCommentBadgeRenderer;
}

class CommentRenderer {
    public AuthorText authorText;
    public AuthorThumbnail authorThumbnail;
    public AuthorEndpoint authorEndpoint;
    public ContentText contentText;
    public PublishedTimeText publishedTimeText;
    public boolean isLiked;
    public int likeCount;
    public String commentId;
    public boolean authorIsChannelOwner;
    public String voteStatus;
    public String trackingParams;
    public VoteCount voteCount;
    public LoggingDirectives loggingDirectives;
    public AuthorCommentBadge authorCommentBadge;
}

class Content {
    public CommentRenderer commentRenderer;
}

class CommentRepliesContinuation {
    public List<Content> contents;
    public List<Continuation> continuations;
    public String trackingParams;
}

class ContinuationContents {
    public CommentRepliesContinuation commentRepliesContinuation;
}

class Response {
    public ContinuationContents continuationContents;
    public String trackingParams;
}

public class Root {
    public String xsrf_token;
    public Response response;
}
