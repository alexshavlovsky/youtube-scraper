package com.ctzn.youtubescraper.addons.hibernateconnector.entity;

import com.ctzn.youtubescraper.core.persistence.dto.ContextStatusDTO;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Embeddable;
import javax.persistence.Lob;
import java.util.Date;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContextStatus {
    Date statusTimestamp;
    StatusCode statusCode;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    String statusMessage;

    public ContextStatus(StatusCode statusCode, String statusMessage) {
        this.statusTimestamp = new Date();
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public ContextStatus(StatusCode statusCode) {
        this(statusCode, null);
    }

    public static ContextStatus fromContextStatusDTO(ContextStatusDTO dto) {
        return new ContextStatus(
                dto.getStatusTimestamp(),
                dto.getStatusCode(),
                dto.getStatusMessage()
        );
    }

}
