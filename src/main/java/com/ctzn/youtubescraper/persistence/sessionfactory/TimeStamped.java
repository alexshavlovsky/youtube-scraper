package com.ctzn.youtubescraper.persistence.sessionfactory;

import java.util.Date;

public interface TimeStamped {
    String CREATED_FIELD_NAME = "createdDate";
    String UPDATED_FIELD_NAME = "lastUpdatedDate";

    Date getCreatedDate();

    void setCreatedDate(Date createdDate);

    Date getLastUpdatedDate();

    void setLastUpdatedDate(Date lastUpdatedDate);
}
