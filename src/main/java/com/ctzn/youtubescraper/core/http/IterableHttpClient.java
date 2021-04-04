package com.ctzn.youtubescraper.core.http;

import com.ctzn.youtubescraper.core.exception.ScraperHttpException;
import com.ctzn.youtubescraper.core.exception.ScraperParserException;
import com.ctzn.youtubescraper.core.exception.ScrapperInterruptedException;
import com.ctzn.youtubescraper.core.model.commons.NextContinuationData;

public interface IterableHttpClient<T> {
    String getParentId();

    T getInitial();

    T requestNext(NextContinuationData continuation) throws ScraperHttpException, ScraperParserException, ScrapperInterruptedException;
}
