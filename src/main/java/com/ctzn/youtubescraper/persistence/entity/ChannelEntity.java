package com.ctzn.youtubescraper.persistence.entity;

import com.ctzn.youtubescraper.model.channelvideos.ChannelDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "channels")
public class ChannelEntity {
    @Id
    @EqualsAndHashCode.Include
    public String channelId;
    public String channelVanityName;
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL)
    public List<VideoEntity> videos;

    public static ChannelEntity fromChannelDTO(ChannelDTO dto) {
        return new ChannelEntity(
                dto.getChannelId(),
                dto.getChannelVanityName(),
                Collections.emptyList()
        );
    }
}
