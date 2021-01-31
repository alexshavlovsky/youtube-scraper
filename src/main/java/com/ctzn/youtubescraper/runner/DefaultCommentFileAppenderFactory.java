package com.ctzn.youtubescraper.runner;

public class DefaultCommentFileAppenderFactory {
    private DefaultCommentFileAppenderFactory() {
    }

    private static class InstanceHolder {
        static CommentFileAppenderFactory instance = new CommentFileAppenderFactory(".output");
    }

    public static CommentFileAppenderFactory getInstance() {
        return InstanceHolder.instance;
    }
}
