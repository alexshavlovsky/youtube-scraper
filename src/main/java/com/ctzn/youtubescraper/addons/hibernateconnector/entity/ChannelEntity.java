package com.ctzn.youtubescraper.addons.hibernateconnector.entity;

import com.ctzn.youtubescraper.addons.hibernateconnector.sessionfactory.TimeStamped;
import com.ctzn.youtubescraper.core.persistence.dto.ChannelDTO;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = false)
@Entity
@ToString(exclude = "videos")
@Table(name = "channels")
public class ChannelEntity implements TimeStamped {
    @Id
    @EqualsAndHashCode.Include
    public String channelId;
    public String channelVanityName;
    public String title;
    public Integer videoCount;
    public Long subscriberCount;
    @OneToMany(mappedBy = "channel")
    public List<VideoEntity> videos;
    @Embedded
    ContextStatus contextStatus;
    public Date createdDate;
    public Date lastUpdatedDate;

    public static ChannelEntity fromChannelDTO(ChannelDTO dto) {
        return new ChannelEntity(
                dto.getChannelId(),
                dto.getChannelVanityName(),
                dto.getTitle(),
                dto.getVideoCount(),
                dto.getSubscriberCount(),
                Collections.emptyList(),
                new ContextStatus(StatusCode.METADATA_FETCHED),
                null,
                null
        );
    }

    public static ChannelEntity newPendingChannel(String channelId) {
        return new ChannelEntity(
                channelId,
                null,
                null,
                null,
                null,
                Collections.emptyList(),
                new ContextStatus(StatusCode.PENDING),
                null,
                null
        );
    }

}
