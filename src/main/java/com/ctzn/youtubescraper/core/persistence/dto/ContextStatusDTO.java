package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

import java.util.Date;

@Value
public class ContextStatusDTO {
    Date statusTimestamp = new Date();
    StatusCode statusCode;
    String statusMessage;
}
