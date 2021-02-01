package com.ctzn.youtubescraper.persistence.repository;

import com.ctzn.youtubescraper.model.DomainMapper;
import com.ctzn.youtubescraper.persistence.entity.CommentEntity;
import org.hibernate.Session;

public class CommentRepository {

    public static void saveOrUpdate(CommentEntity comment, Session session) {
        CommentEntity entity = session.get(CommentEntity.class, comment.getCommentId());
        if (entity == null) session.save(comment);
        else DomainMapper.getInstance().map(comment, entity);
    }
}
