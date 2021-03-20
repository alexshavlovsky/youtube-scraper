package com.ctzn.youtubescraper.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CountLimitTest {

    @Test
    void test() {
        // new instance
        CountLimit countLimit = new CountLimit();
        assertThrows(IllegalStateException.class, countLimit::get);
        assertFalse(countLimit.isLimitedToZero());
        assertTrue(countLimit.isUnrestricted());
        assertTrue(countLimit.isValueBelowLimit(36));
        assertTrue(countLimit.isValueBelowLimit(37));
        assertTrue(countLimit.isValueBelowLimit(38));
        // after set value
        countLimit.set(37);
        assertEquals(37, countLimit.get());
        assertFalse(countLimit.isLimitedToZero());
        assertFalse(countLimit.isUnrestricted());
        assertTrue(countLimit.isValueBelowLimit(36));
        assertFalse(countLimit.isValueBelowLimit(37));
        assertFalse(countLimit.isValueBelowLimit(38));
        // after set to zero
        countLimit.set(0);
        assertEquals(0, countLimit.get());
        assertTrue(countLimit.isLimitedToZero());
        assertFalse(countLimit.isUnrestricted());
        assertFalse(countLimit.isValueBelowLimit(36));
        assertFalse(countLimit.isValueBelowLimit(37));
        assertFalse(countLimit.isValueBelowLimit(38));
        // after reset
        countLimit.reset();
        assertThrows(IllegalStateException.class, countLimit::get);
        assertFalse(countLimit.isLimitedToZero());
        assertTrue(countLimit.isUnrestricted());
        assertTrue(countLimit.isValueBelowLimit(36));
        assertTrue(countLimit.isValueBelowLimit(37));
        assertTrue(countLimit.isValueBelowLimit(38));
    }

}
