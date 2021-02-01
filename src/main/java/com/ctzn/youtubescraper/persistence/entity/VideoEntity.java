package com.ctzn.youtubescraper.persistence.entity;

import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;
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
@Table(name = "videos")
public class VideoEntity implements TimeStamped {
    @Id
    @EqualsAndHashCode.Include
    String videoId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    ChannelEntity channel;
    String title;
    String publishedTimeText;
    int viewCountText;
    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL)
    List<CommentEntity> comments;
    public Date createdDate;
    public Date lastUpdatedDate;

    public static VideoEntity fromVideoDTO(VideoDTO dto, ChannelEntity channel) {
        return new VideoEntity(
                dto.getVideoId(),
                channel,
                dto.getTitle(),
                dto.getPublishedTimeText(),
                dto.getViewCountText(),
                Collections.emptyList(),
                null,
                null
        );
    }
}
