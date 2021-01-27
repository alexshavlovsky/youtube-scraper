package com.ctzn.youtubescraper.entity;

import com.ctzn.youtubescraper.model.CommentDTO;
import lombok.*;

import javax.persistence.*;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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

    public static CommentEntity fromCommentDTO(CommentDTO dto, Map<String, VideoEntity> videoEntityMap, Map<String, CommentEntity> commentEntityMap) {
        return new CommentEntity(
                dto.getCommentId(),
                videoEntityMap.get(dto.getVideoId()),
                dto.getAuthorText(),
                dto.getAuthorUrl(),
                dto.getPublishedTimeText(),
                dto.getText(),
                dto.getLikeCount(),
                dto.getReplyCount(),
                dto.getParentCommentId() == null || commentEntityMap == null ? null : commentEntityMap.get(dto.getParentCommentId()),
                Collections.emptyList()
        );
    }
}
