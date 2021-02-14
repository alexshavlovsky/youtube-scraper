package com.ctzn.youtubescraper.commentformatter;

import com.ctzn.youtubescraper.model.comments.CommentDTO;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.Function;

public class CommentHumanReadableFormatter extends CommentFormatter {

    private static class Joiner {
        private final String prefix, suffix, delimiter;
        private final StringBuilder builder = new StringBuilder();

        Joiner(String prefix, String suffix, String delimiter) {
            this.prefix = prefix;
            this.suffix = suffix;
            this.delimiter = delimiter;
        }

        private void join(String prefix, String suffix, String delimiter, String... text) {
            builder.append(prefix).append(String.join(delimiter, text)).append(suffix);
        }

        void join(String... text) {
            join(prefix, suffix, delimiter, text);
        }

        void joinNoSuffix(String... text) {
            join(prefix, "", delimiter, text);
        }

        String getResult() {
            return builder.toString();
        }
    }

    private static String multiLine(String longString, String splitter, int maxLength) {
        return Arrays.stream(longString.split(splitter)).collect(ArrayList<String>::new, (l, s) -> {
            Function<ArrayList<String>, Integer> id = list -> list.size() - 1;
            if (l.size() == 0 || (l.get(id.apply(l)).length() != 0 && l.get(id.apply(l)).length() + s.length() >= maxLength))
                l.add("");
            l.set(id.apply(l), l.get(id.apply(l)) + (l.get(id.apply(l)).length() == 0 ? "" : splitter) + s);
        }, ArrayList::addAll).stream().reduce((s1, s2) -> s1 + "\n" + s2).get();
    }

    @Override
    String formatToString(CommentDTO comment) {
        boolean isReply = comment.parentCommentId != null;
        String offs = isReply ? "\t" : "";
        String link = String.format("/watch?v=%s&lc=%s", comment.videoId, comment.commentId);
        String author = String.format("%s %s", comment.authorText, comment.channelId);
        int lineLength = Math.max(link.length(), author.length());
        String header = (isReply ? "-" : "=").repeat(lineLength + 8);
        String text = multiLine(comment.text, " ", lineLength).replaceAll("\n", "\n        " + offs);
        Joiner joiner = new Joiner(offs, "\n", "");
        joiner.join(header);
        joiner.join("Link:   ", link);
        joiner.join("Author: ", author);
        joiner.join("Text:   ", text);
        joiner.joinNoSuffix(String.format("        %s", comment.publishedTimeText));
        if (comment.likeCount > 0) joiner.joinNoSuffix(String.format("    likes %s", comment.likeCount));
        if (comment.replyCount > 0) joiner.joinNoSuffix(String.format("    replies %s", comment.replyCount));
        return joiner.getResult();
    }
}
