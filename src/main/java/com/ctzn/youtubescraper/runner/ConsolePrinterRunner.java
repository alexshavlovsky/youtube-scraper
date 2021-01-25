package com.ctzn.youtubescraper.runner;

import com.ctzn.youtubescraper.formatter.CommentHumanReadableFormatter;
import com.ctzn.youtubescraper.handler.CommentConsolePrinter;
import lombok.extern.java.Log;

import java.util.List;

@Log
abstract class ConsolePrinterRunner extends AbstractRunner {

    ConsolePrinterRunner(String videoId) {
        super(videoId, List.of(
                new CommentConsolePrinter(new CommentHumanReadableFormatter())
        ));
    }
}
