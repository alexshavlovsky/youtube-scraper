package com.ctzn.youtubescraper.db;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

public class PersistenceContext {

    private static final SessionFactory factory = SessionFactorySource.getInstance().getSessionFactory();

    private PersistenceContext() {
    }

    public static void commitTransaction(PersistenceStrategy persistenceStrategy) {
        Transaction tx = null;
        try (Session session = factory.openSession()) {
            tx = session.beginTransaction();
            persistenceStrategy.execute(session);
            tx.commit();
        } catch (PersistenceException ex) {
            if (tx != null) tx.rollback();
        }
    }
}
