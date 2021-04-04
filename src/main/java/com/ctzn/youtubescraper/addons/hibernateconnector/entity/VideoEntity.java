package com.ctzn.youtubescraper.addons.hibernateconnector.entity;

import com.ctzn.youtubescraper.addons.hibernateconnector.sessionfactory.TimeStamped;
import com.ctzn.youtubescraper.core.persistence.dto.StatusCode;
import com.ctzn.youtubescraper.core.persistence.dto.VideoDTO;
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
    @OneToMany(mappedBy = "video")
    List<CommentEntity> comments;
    @Embedded
    ContextStatus contextStatus;
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
                new ContextStatus(StatusCode.METADATA_FETCHED),
                null,
                null
        );
    }

}
