package com.ctzn.youtubescraper.persistence;

import com.ctzn.youtubescraper.persistence.sessionfactory.AbstractSessionFactory;
import lombok.extern.java.Log;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.PersistenceException;
import java.util.function.Function;

@Log
public class DefaultPersistenceContext implements PersistenceContext {

    @Override
    public <T> T commitTransactionAndGetResult(Function<Session, T> supplierPersistenceStrategy) {
        boolean interrupted = Thread.interrupted();
        Transaction tx = null;
        try (Session session = AbstractSessionFactory.getInstance().openSession()) {
            tx = session.beginTransaction();
            T result = supplierPersistenceStrategy.apply(session);
            tx.commit();
            return result;
        } catch (PersistenceException ex) {
            log.severe("Transaction rollback: " + ex.getMessage());
            if (tx != null) tx.rollback();
        } finally {
            if (interrupted) Thread.currentThread().interrupt();
        }
        return null;
    }

}
