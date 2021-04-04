package com.ctzn.youtubescraper.core.persistence.dto;

import lombok.Value;

@Value
public class WorkerLogDTO {
    String contextId;
    ContextStatusDTO contextStatus;
}
