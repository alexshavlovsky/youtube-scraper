package com.ctzn.youtubescraper.core.handler;

import java.util.List;
import java.util.function.Consumer;

@FunctionalInterface
public interface DataHandler<T> extends Consumer<List<T>> {

    static <T> DataHandler<T> from(List<DataHandler<T>> list) {
        return new MultipleHandler<>(list);
    }

    static <T> DataHandler<T> of(DataHandler<T> h1, DataHandler<T> h2) {
        return new MultipleHandler<>(List.of(h1, h2));
    }

    static <T> DataHandler<T> of(DataHandler<T> h1, DataHandler<T> h2, DataHandler<T> h3) {
        return new MultipleHandler<>(List.of(h1, h2, h3));
    }

    static <T> DataHandler<T> of(DataHandler<T> h1, DataHandler<T> h2, DataHandler<T> h3, DataHandler<T> h4) {
        return new MultipleHandler<>(List.of(h1, h2, h3, h4));
    }

    class MultipleHandler<E> implements DataHandler<E> {

        private final List<DataHandler<E>> handlers;

        MultipleHandler(List<DataHandler<E>> handlers) {
            this.handlers = handlers;
        }

        @Override
        public void accept(List<E> es) {
            handlers.forEach(handler -> handler.accept(es));
        }

    }

}
