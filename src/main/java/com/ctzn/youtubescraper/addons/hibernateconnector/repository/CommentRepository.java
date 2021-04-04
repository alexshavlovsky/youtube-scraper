package com.ctzn.youtubescraper.addons.hibernateconnector.repository;

import com.ctzn.youtubescraper.core.persistence.dto.CommentDTO;
import com.ctzn.youtubescraper.addons.hibernateconnector.DomainMapper;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.CommentEntity;
import com.ctzn.youtubescraper.addons.hibernateconnector.entity.VideoEntity;
import org.hibernate.Session;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentRepository {

    public static CommentEntity findById(String commentId, Session session) {
        return session.get(CommentEntity.class, commentId);
    }

    public static CommentEntity saveOrUpdateAndGet(CommentDTO commentDTO, CommentEntity parentComment, VideoEntity videoEntity, Session session) {
        CommentEntity persistentComment = findById(commentDTO.getCommentId(), session);
        if (persistentComment == null) {
            CommentEntity transientComment = CommentEntity.fromCommentDTO(videoEntity, parentComment, commentDTO);
            session.persist(transientComment);
            return transientComment;
        } else {
            DomainMapper.getInstance().map(commentDTO, persistentComment);
            // manually update the last updated date
            persistentComment.setLastUpdatedDate(new Date());
            return persistentComment;
        }
    }

    public static HashMap<String, CommentEntity> saveAll(Collection<CommentDTO> comments, Map<String, CommentEntity> parentMap, VideoEntity videoEntity, Session session) {
        return comments.stream().map(comment -> saveOrUpdateAndGet(comment, parentMap == null ? null : parentMap.get(comment.getParentCommentId()), videoEntity, session))
                .collect(HashMap::new, (map, comment) -> map.put(comment.getCommentId(), comment), Map::putAll);
    }

}
