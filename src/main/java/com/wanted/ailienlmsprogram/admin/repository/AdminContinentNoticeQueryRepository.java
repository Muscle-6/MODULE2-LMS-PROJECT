package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeContinentResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeWritePageResponse;
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

    public List<AdminContinentNoticeContinentResponse> findContinentsForNotice() {
        List<Object[]> rows = em.createQuery("""
            select
                c.continentId,
                c.continentName,
                c.description,
                (select count(p.postId)
                   from AdminContinentNoticePost p
                  where p.continentId = c.continentId
                    and p.postIsNotice = true
                    and p.postIsDeleted = false)
            from Continent c
            order by c.continentName asc
            """, Object[].class)
                .getResultList();

        return rows.stream()
                .map(row -> new AdminContinentNoticeContinentResponse(
                        (Long) row[0],
                        (String) row[1],
                        (String) row[2],
                        row[3] == null ? 0L : (Long) row[3]
                ))
                .toList();
    }

    public AdminContinentNoticeWritePageResponse findWriteTarget(Long continentId) {
        try {
            return em.createQuery("""
                select new com.wanted.ailienlmsprogram.admin.dto.AdminContinentNoticeWritePageResponse(
                    c.continentId,
                    c.continentName,
                    c.description
                )
                from Continent c
                where c.continentId = :continentId
                """, AdminContinentNoticeWritePageResponse.class)
                    .setParameter("continentId", continentId)
                    .getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Continent findContinent(Long continentId) {
        return em.find(Continent.class, continentId);
    }

    public Member findMember(Long memberId) {
        return em.find(Member.class, memberId);
    }
}