package com.wanted.ailienlmsprogram.community.repository;

import com.wanted.ailienlmsprogram.community.entity.CommunityComment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<CommunityComment, Long> {


    List<CommunityComment> findByPostPostIdAndIsDeletedFalse(Long postId);}