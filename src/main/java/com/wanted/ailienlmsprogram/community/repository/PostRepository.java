package com.wanted.ailienlmsprogram.community.repository;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<CommunityPost, Long> {
    //쿼리 메서드 .... 대륙 ID로 찾고, 삭제안된것만
    List<CommunityPost> findByContinentIdAndPostIsDeletedFalse(Long continentId);
}