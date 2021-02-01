package com.ctzn.youtubescraper.persistence.entity;

import com.ctzn.youtubescraper.model.channelvideos.VideoDTO;
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
@Table(name = "videos")
public class VideoEntity {
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

    public static VideoEntity fromVideoDTO(VideoDTO dto, ChannelEntity channel) {
        return new VideoEntity(
                dto.getVideoId(),
                channel,
                dto.getTitle(),
                dto.getPublishedTimeText(),
                dto.getViewCountText(),
                Collections.emptyList()
        );
    }
}
