package com.ctzn.youtubescraper.persistence.runner;

import com.ctzn.youtubescraper.iterator.comment.CommentVisitor;
import com.ctzn.youtubescraper.persistence.DefaultPersistenceContext;
import com.ctzn.youtubescraper.persistence.PersistenceContext;
import lombok.Builder;
import lombok.NonNull;

import java.util.concurrent.TimeUnit;

@Builder(buildMethodName = "getBuilder")
public class PersistenceChannelRunnerConfigurer {
    @NonNull String channelId;
    @NonNull PersistenceContext persistenceContext;
    @Builder.Default
    int nThreads = 20;
    @Builder.Default
    long timeout = 24;
    @Builder.Default
    TimeUnit timeUnit = TimeUnit.HOURS;
    @Builder.Default
    boolean sortNewestCommentsFirst = true;
    @Builder.Default
    int videoCountLimit = CommentVisitor.NO_LIMIT;
    @Builder.Default
    int commentCountPerVideoLimit = CommentVisitor.NO_LIMIT;
    @Builder.Default
    int replyThreadCountLimit = PersistenceChannelRunner.PROCESS_ALL_VIDEOS;

    public PersistenceChannelRunner build() {
        return new PersistenceChannelRunner(channelId, persistenceContext, nThreads, timeout, timeUnit, sortNewestCommentsFirst, commentCountPerVideoLimit, replyThreadCountLimit, videoCountLimit);
    }

    static PersistenceChannelRunnerConfigurerBuilder newInstance(String channelId) {
        return builder().channelId(channelId).persistenceContext(new DefaultPersistenceContext());
    }
}
