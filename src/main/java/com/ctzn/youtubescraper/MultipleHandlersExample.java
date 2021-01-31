package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.handler.CommentHandler;
import com.ctzn.youtubescraper.runner.CommentFileAppenderFactory;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.List;

public class MultipleHandlersExample {

    public static void main(String[] args) {
        String videoId = "ipAnwilMncI";
        CommentHandler consolePrinter = new CommentConsolePrinter(new CommentHumanReadableFormatter());
        CommentHandler fileAppender = new CommentFileAppenderFactory(".output").newInstance(videoId);
        List<CommentHandler> handlers = List.of(
                consolePrinter,
                fileAppender
        );
        Runnable runner = CommentRunnerFactory
                .newInstance(videoId, handlers, false, 1000, 10);
        runner.run();
    }
}
