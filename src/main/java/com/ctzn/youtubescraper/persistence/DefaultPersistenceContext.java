package com.ctzn.youtubescraper.persistence;

import com.ctzn.youtubescraper.persistence.sessionfactory.H2DBSessionFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

public class DefaultPersistenceContext implements PersistenceContext {

    private final SessionFactory sessionFactory = H2DBSessionFactory.getInstance();

    @Override
    public void commitTransaction(PersistenceStrategy persistenceStrategy) {
        boolean interrupted = Thread.interrupted();
        Transaction tx = null;
        try (Session session = sessionFactory.openSession()) {
            tx = session.beginTransaction();
            persistenceStrategy.execute(session);
            tx.commit();
        } catch (PersistenceException ex) {
            if (tx != null) tx.rollback();
        } finally {
            if (interrupted) Thread.currentThread().interrupt();
        }
    }
}
