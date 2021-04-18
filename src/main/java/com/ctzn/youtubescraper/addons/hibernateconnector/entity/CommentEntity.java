package com.ctzn.youtubescraper.addons.hibernateconnector.entity;

import com.ctzn.youtubescraper.addons.hibernateconnector.sessionfactory.TimeStamped;
import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import lombok.*;
import org.hibernate.annotations.Type;

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
    public Date publishedDate;
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    public String text;
    public int likeCount;
    public int replyCount;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    public CommentEntity parent;
    @OneToMany(mappedBy = "parent")
    List<CommentEntity> replies;
    public Date createdDate;
    public Date lastUpdatedDate;

    public String getVideoId() {
        return video.getVideoId();
    }

    public String getParentId() {
        return parent == null ? null : parent.getCommentId();
    }

    public static CommentEntity fromCommentDTO(VideoEntity videoEntity, CommentEntity parentComment, CommentDTO dto) {
        return new CommentEntity(
                dto.getCommentId(),
                videoEntity,
                dto.getAuthorText(),
                dto.getChannelId(),
                dto.getPublishedTimeText(),
                dto.getPublishedDate(),
                dto.getText(),
                dto.getLikeCount(),
                dto.getReplyCount(),
                parentComment,
                Collections.emptyList(),
                null,
                null
        );
    }

}
