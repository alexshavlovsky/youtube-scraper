package com.ctzn.youtubescraper.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentOrderCfg {

    public enum CommentOrder {
        TOP_FIRST, NEWEST_FIRST
    }

    public CommentOrder commentOrder = CommentOrder.NEWEST_FIRST;

    public static CommentOrderCfg topFirst() {
        return new CommentOrderCfg(CommentOrder.TOP_FIRST);
    }

    public static CommentOrderCfg newestFirst() {
        return new CommentOrderCfg(CommentOrder.NEWEST_FIRST);
    }

}
