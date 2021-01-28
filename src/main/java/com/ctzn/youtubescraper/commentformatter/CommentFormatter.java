package com.ctzn.youtubescraper.commentformatter;

import com.ctzn.youtubescraper.model.CommentDTO;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public abstract class CommentFormatter {
    abstract String formatToString(CommentDTO comment);

    public void printComment(CommentDTO comment) {
        System.out.println(formatToString(comment));
    }

    public void printAll(List<CommentDTO> list) {
        list.forEach(this::printComment);
    }

    public void appendAll(String file, List<CommentDTO> list) {
        try (FileWriter fw = new FileWriter(file, true);
             BufferedWriter bw = new BufferedWriter(fw)) {
            for (CommentDTO comment : list) {
                bw.write(formatToString(comment));
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeAll(String file, List<CommentDTO> list) {
        try {
            Files.write(Path.of(file), list.stream().map(this::formatToString).collect(Collectors.toList()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
