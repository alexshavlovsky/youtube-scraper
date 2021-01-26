package com.ctzn.youtubescraper.entity;

import lombok.*;

import javax.persistence.*;
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
}
