package com.ctzn.youtubescraper.http;

import com.ctzn.youtubescraper.exception.ScraperHttpException;
import com.ctzn.youtubescraper.exception.ScraperParserException;
import com.ctzn.youtubescraper.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.model.commons.NextContinuationData;

public interface IterableHttpClient<T> {
    String getParentId();

    T getInitial();

    T requestNext(NextContinuationData continuation) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException;
}
