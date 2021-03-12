package com.ctzn.youtubescraper;

import com.ctzn.youtubescraper.commentformatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import com.ctzn.youtubescraper.runner.CommentFileAppenderFactory;
import com.ctzn.youtubescraper.runner.CommentRunnerFactory;

import java.util.List;

public class MultipleHandlersExample {

    public static void main(String[] args) {
        String videoId = "ipAnwilMncI";
        Runnable runner = CommentRunnerFactory.newInstance(
                videoId,
                List.of(
                        new CommentConsolePrinter(new CommentHumanReadableFormatter()),
                        new CommentFileAppenderFactory(".output").newInstance(videoId)
                ),
                false,
                1000,
                10
        );
        runner.run();
    }

}
