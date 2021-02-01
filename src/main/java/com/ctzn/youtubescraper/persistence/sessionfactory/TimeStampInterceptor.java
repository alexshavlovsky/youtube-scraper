package com.ctzn.youtubescraper.persistence.sessionfactory;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;

import java.io.Serializable;
import java.util.Date;

public class TimeStampInterceptor extends EmptyInterceptor {

    private static int indexOf(String[] array, String value) {
        for (int i = 0; i < array.length; i++) if (value.equals(array[i])) return i;
        return -1;
    }

    private boolean updateDateProperty(Object entity, Object[] currentState, String[] propertyNames, String propertyName) {
        if (entity instanceof TimeStamped) {
            int propertyIndex = indexOf(propertyNames, propertyName);
            if (propertyIndex == -1) return false;
            currentState[propertyIndex] = new Date();
            return true;
        }
        return false;
    }

    @Override
    public boolean onFlushDirty(Object entity, Serializable id, Object[] currentState, Object[] previousState, String[] propertyNames, Type[] types) {
        return updateDateProperty(entity, currentState, propertyNames, TimeStamped.UPDATED_FIELD_NAME);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return updateDateProperty(entity, state, propertyNames, TimeStamped.CREATED_FIELD_NAME);
    }
}
