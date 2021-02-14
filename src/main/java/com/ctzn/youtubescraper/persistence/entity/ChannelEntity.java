package com.ctzn.youtubescraper.persistence.entity;

import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import com.ctzn.youtubescraper.persistence.sessionfactory.TimeStamped;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "channels")
public class ChannelEntity implements TimeStamped {
    @Id
    @EqualsAndHashCode.Include
    public String channelId;
    public String channelVanityName;
    public String title;
    public String subscriberCountText;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    public List<VideoEntity> videos;
    public Date createdDate;
    public Date lastUpdatedDate;

    public static ChannelEntity fromChannelDTO(ChannelDTO dto) {
        return new ChannelEntity(
                dto.getChannelId(),
                dto.getChannelVanityName(),
                dto.getTitle(),
                dto.getSubscriberCountText(),
                Collections.emptyList(),
                null,
                null
        );
    }
}
