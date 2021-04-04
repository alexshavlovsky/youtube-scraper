package com.ctzn.youtubescraper.examples.filesystem;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.handler.DataHandler;
import com.ctzn.youtubescraper.core.persistence.CommentRunnerFactory;
import com.ctzn.youtubescraper.addons.customhandlers.CommentFileAppenderFactory;
import com.ctzn.youtubescraper.addons.customhandlers.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.addons.customhandlers.CommentConsolePrinter;

public class MultipleHandlersExample {

    public static void main(String[] args) {
        String videoId = "ipAnwilMncI";
        Runnable runner = CommentRunnerFactory.newInstance(
                videoId,
                DataHandler.of(
                        new CommentConsolePrinter(new CommentHumanReadableFormatter()),
                        new CommentFileAppenderFactory(".output").newInstance(videoId)
                ),
                CommentOrderCfg.TOP_FIRST,
                CommentIteratorCfg.newInstance(1000, 10)
        );
        runner.run();
    }

}
