package com.ctzn.youtubescraper.persistence.entity;

import com.ctzn.youtubescraper.model.comments.CommentDTO;
import com.ctzn.youtubescraper.persistence.sessionfactory.TimeStamped;
import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
@Table(name = "comments")
public class CommentEntity implements TimeStamped {
    @Id
    @EqualsAndHashCode.Include
    public String commentId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id")
    public VideoEntity video;
    public String authorText;
    public String channelId;
    public String publishedTimeText;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String text;
    public int likeCount;
    public int replyCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CommentEntity parent;
    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL)
    List<CommentEntity> replies;
    public Date createdDate;
    public Date lastUpdatedDate;

    public static CommentEntity fromCommentDTO(CommentDTO dto, Map<String, VideoEntity> videoEntityMap, Map<String, CommentEntity> commentEntityMap) {
        return new CommentEntity(
                dto.getCommentId(),
                videoEntityMap.get(dto.getVideoId()),
                dto.getAuthorText(),
                dto.getChannelId(),
                dto.getPublishedTimeText(),
                dto.getText(),
                dto.getLikeCount(),
                dto.getReplyCount(),
                dto.getParentCommentId() == null || commentEntityMap == null ? null : commentEntityMap.get(dto.getParentCommentId()),
                Collections.emptyList(),
                null,
                null
        );
    }
}
