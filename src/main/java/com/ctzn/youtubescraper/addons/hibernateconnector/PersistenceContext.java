package com.ctzn.youtubescraper.addons.hibernateconnector;

import org.hibernate.Session;

import java.util.function.Consumer;
import java.util.function.Function;

public interface PersistenceContext {
    default void commitTransaction(Consumer<Session> persistenceStrategy) {
        commitTransactionAndGetResult(session -> {
            persistenceStrategy.accept(session);
            return Void.TYPE;
        });
    }

    <T> T commitTransactionAndGetResult(Function<Session, T> supplierPersistenceStrategy);
}
