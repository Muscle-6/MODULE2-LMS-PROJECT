package com.wanted.ailienlmsprogram.user.repository;

import com.wanted.ailienlmsprogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // loginId 로 조회 - 추후 getMemberId() 추가되면 findById() 로 변경 예정
    Optional<User> findByLoginId(String loginId);
}