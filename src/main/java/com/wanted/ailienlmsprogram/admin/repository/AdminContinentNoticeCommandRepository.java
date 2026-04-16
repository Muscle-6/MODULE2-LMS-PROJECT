package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.entity.AdminContinentNoticePost;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class AdminContinentNoticeCommandRepository {

    @PersistenceContext
    private EntityManager em;

    public void save(AdminContinentNoticePost noticePost) {
        em.persist(noticePost);
    }
}