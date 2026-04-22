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

/*관리자 회원 조회 전용 쿼리 리포지토리.
*
* - 검색 조건에 따라 JPQL을 동적으로 생성한다.
* - 회원 목록 화면에 필요한 데이터만 DTO로 조회한다.
* - 회원 단건 조회 기능을 제공한다.*/
@Repository
public class AdminMemberQueryRepository {

    @PersistenceContext
    private EntityManager em;

    //검색 조건에 맞는 회원 목록을 조회한다.
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

    //화원 id로 회원 엔티티를 조회한다.
    public Member findMember(Long memberId) {
        return em.find(Member.class, memberId);
    }

    public void delete(Member member) {
        Member managedMember = em.contains(member) ? member : em.merge(member);
        em.remove(managedMember);
    }
}