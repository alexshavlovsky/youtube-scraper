package com.ctzn.youtubescraper.persistence;

import org.hibernate.type.StringType;

import java.util.List;

public class NativeQueryExecutor {

    private static final PersistenceContext persistenceContext = new DefaultPersistenceContext();

    public static String getSingleStringResultByColumnAlias(String query, String columnAlias) {
        return persistenceContext.commitTransactionAndGetResult(session -> {
            @SuppressWarnings("unchecked")
            List<String> res = session.createNativeQuery(query).addScalar(columnAlias, StringType.INSTANCE).list();
            if (res.size() == 0) return null;
            return res.get(0);
        });
    }

}
