package com.ctzn.youtubescraper.config;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class CommentOrderCfg {

    public enum CommentOrderEnum {
        TOP_FIRST, NEWEST_FIRST
    }

    private final CommentOrderEnum commentOrder;

    public static CommentOrderCfg TOP_FIRST = new CommentOrderCfg(CommentOrderEnum.TOP_FIRST);

    public static CommentOrderCfg NEWEST_FIRST = new CommentOrderCfg(CommentOrderEnum.NEWEST_FIRST);

    public boolean isNewestFirst() {
        return commentOrder == CommentOrderEnum.NEWEST_FIRST;
    }

    public boolean isTopFirst() {
        return commentOrder == CommentOrderEnum.TOP_FIRST;
    }

}
