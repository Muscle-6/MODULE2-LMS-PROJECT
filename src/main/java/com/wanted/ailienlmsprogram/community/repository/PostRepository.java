package com.wanted.ailienlmsprogram.community.repository;

import com.wanted.ailienlmsprogram.community.entity.CommunityPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<CommunityPost, Long> {

    @Query("SELECT p FROM CommunityPost p " +
            "WHERE p.continentId = :continentId AND p.postIsDeleted = false " +
            "ORDER BY p.postIsNotice DESC, p.postId DESC")
    List<CommunityPost> findActivePostsByContinent(@Param("continentId") Long continentId);
}