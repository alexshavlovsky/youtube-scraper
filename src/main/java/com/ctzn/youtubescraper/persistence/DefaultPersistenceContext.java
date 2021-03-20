package com.ctzn.youtubescraper.persistence;

import com.ctzn.youtubescraper.persistence.sessionfactory.AbstractSessionFactory;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;

@Log
public class DefaultPersistenceContext implements PersistenceContext {

    @Override
    public void commitTransaction(PersistenceStrategy persistenceStrategy) {
        boolean interrupted = Thread.interrupted();
        Transaction tx = null;
        try (Session session = AbstractSessionFactory.getInstance().openSession()) {
            tx = session.beginTransaction();
            persistenceStrategy.execute(session);
            tx.commit();
        } catch (PersistenceException ex) {
            log.severe("Transaction rollback: " + ex.getMessage());
            if (tx != null) tx.rollback();
        } finally {
            if (interrupted) Thread.currentThread().interrupt();
        }
    }

}
