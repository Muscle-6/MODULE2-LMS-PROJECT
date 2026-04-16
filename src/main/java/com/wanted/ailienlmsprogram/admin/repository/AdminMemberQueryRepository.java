package com.wanted.ailienlmsprogram.admin.repository;

import com.wanted.ailienlmsprogram.admin.dto.AdminMemberListResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminMemberSearchCondition;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class AdminMemberQueryRepository {

    @PersistenceContext
    private EntityManager em;

    public List<AdminMemberListResponse> findMembers(AdminMemberSearchCondition condition) {
        StringBuilder jpql = new StringBuilder("""
            select new com.wanted.ailienlmsprogram.admin.dto.AdminMemberListResponse(
                m.memberId,
                m.loginId,
                m.name,
                m.email,
                m.phone,
                m.role,
                m.rank,
                m.createdAt,
                m.lastLoginAt,
                m.accountStatus,
                m.deletedAt
            )
            from Member m
            where 1=1
        """);

        if (condition.getRole() != null) {
            jpql.append(" and m.role = :role ");
        }

        if (StringUtils.hasText(condition.getQuery())) {
            jpql.append("""
                and (
                    lower(m.loginId) like lower(concat('%', :query, '%'))
                    or lower(m.name) like lower(concat('%', :query, '%'))
                    or lower(m.email) like lower(concat('%', :query, '%'))
                )
            """);
        }

        jpql.append(" order by m.createdAt desc, m.memberId desc ");

        TypedQuery<AdminMemberListResponse> query =
                em.createQuery(jpql.toString(), AdminMemberListResponse.class);

        if (condition.getRole() != null) {
            query.setParameter("role", condition.getRole());
        }
        if (StringUtils.hasText(condition.getQuery())) {
            query.setParameter("query", condition.getQuery().trim());
        }

        return query.getResultList();
    }

    public Member findMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public void delete(Member member) {
        Member managedMember = em.contains(member) ? member : em.merge(member);
        em.remove(managedMember);
    }
}