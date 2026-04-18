package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeListResponse;
import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminContinentNoticeQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<AdminContinentNoticeContinentResponse> findContinents() {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse(
                    c.continentId,
                    c.continentName,
                    c.description
                )
                from Continent c
                order by c.continentName asc
                """, AdminContinentNoticeContinentResponse.class)
                .getResultList();
    }

    public AdminContinentNoticeContinentResponse findContinent(Long continentId) {
        try {
            return em.createQuery("""
                    select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse(
                        c.continentId,
                        c.continentName,
                        c.description
                    )
                    from Continent c
                    where c.continentId = :continentId
                    """, AdminContinentNoticeContinentResponse.class)
                    .setParameter("continentId", continentId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public List<AdminContinentNoticeListResponse> findNoticePostsByContinent(Long continentId) {
        return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeListResponse(
                    p.postId,
                    p.postTitle,
                    m.name,
                    p.createdAt
                )
                from CommunityPost p
                join p.member m
                where p.continentId = :continentId
                  and p.postIsDeleted = false
                  and p.postIsNotice = true
                order by p.createdAt desc, p.postId desc
                """, AdminContinentNoticeListResponse.class)
                .setParameter("continentId", continentId)
                .getResultList();
    }

    public AdminContinentNoticeDetailResponse findNoticeDetail(Long continentId, Long postId) {
        try {
            return em.createQuery("""
                    select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeDetailResponse(
                        p.postId,
                        c.continentId,
                        c.continentName,
                        c.description,
                        p.postTitle,
                        p.postContent,
                        m.name,
                        p.createdAt
                    )
                    from CommunityPost p
                    join p.member m, Continent c
                    where p.postId = :postId
                      and p.continentId = :continentId
                      and p.continentId = c.continentId
                      and p.postIsDeleted = false
                      and p.postIsNotice = true
                    """, AdminContinentNoticeDetailResponse.class)
                    .setParameter("continentId", continentId)
                    .setParameter("postId", postId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public CommunityPost findPost(Long postId) {
        return em.find(CommunityPost.class, postId);
    }

    public Member findMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public Continent findContinentEntity(Long continentId) {
        return em.find(Continent.class, continentId);
    }

    public void save(CommunityPost post) {
        if (post.getPostId() == null) {
            em.persist(post);
            return;
        }
        em.merge(post);
    }
}