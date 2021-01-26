package com.ctzn.youtubescraper.entity;

import com.ctzn.youtubescraper.model.ChannelDTO;
import com.ctzn.youtubescraper.model.CommentDTO;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
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
}
