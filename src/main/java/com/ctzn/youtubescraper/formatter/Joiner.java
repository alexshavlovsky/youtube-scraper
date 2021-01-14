package com.ctzn.youtubescraper.formatter;

class Joiner {
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
