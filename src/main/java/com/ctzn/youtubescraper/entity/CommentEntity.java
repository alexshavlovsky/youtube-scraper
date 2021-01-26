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
@Table(name = "comments")
public class CommentEntity {
    @Id
    @EqualsAndHashCode.Include
    public String commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    public VideoEntity video;
    public String authorText;
    public String authorUrl;
    public String publishedTimeText;
    @Lob
    public String text;
    public int likeCount;
    public int replyCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CommentEntity parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    List<CommentEntity> replies;
}
