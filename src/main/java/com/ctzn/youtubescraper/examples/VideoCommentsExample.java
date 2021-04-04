package com.ctzn.youtubescraper.examples;

import com.ctzn.youtubescraper.core.config.CommentIteratorCfg;
import com.ctzn.youtubescraper.core.config.CommentOrderCfg;
import com.ctzn.youtubescraper.core.persistence.CommentRunnerFactory;
import com.ctzn.youtubescraper.addons.customhandlers.CommentConsolePrinter;
import com.ctzn.youtubescraper.addons.customhandlers.CommentHumanReadableFormatter;

public class VideoCommentsExample {

    public static void main(String[] args) {
        String videoId = "ipAnwilMncI";
        Runnable runner = CommentRunnerFactory.newInstance(
                videoId,
                new CommentConsolePrinter(new CommentHumanReadableFormatter()),
                CommentOrderCfg.TOP_FIRST,
                CommentIteratorCfg.newInstance(1000, 10)
        );
        runner.run();
    }

}
