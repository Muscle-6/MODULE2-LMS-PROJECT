package com.wanted.ailienlmsprogram.user.repository;

import com.wanted.ailienlmsprogram.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
}