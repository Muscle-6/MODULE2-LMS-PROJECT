package com.wanted.ailienlmsprogram.member.repository;

import com.wanted.ailienlmsprogram.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/*member 테이블 접근용 Repository*/
public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByLoginId(String loginId);

    boolean existsByLoginId(String loginId);

    boolean existsByEmail(String email);
}