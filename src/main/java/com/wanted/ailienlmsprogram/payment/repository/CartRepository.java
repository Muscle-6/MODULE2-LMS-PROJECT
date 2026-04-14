package com.wanted.ailienlmsprogram.payment.repository;

import com.wanted.ailienlmsprogram.payment.dto.CartItemResponse;
import com.wanted.ailienlmsprogram.payment.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {

    boolean existsByCourseCourseIdAndMemberMemberId(Long courseId, Long memberId);

    List<Cart> findByMemberMemberId(Long memberId);

    @Query("""
            SELECT new com.wanted.ailienlmsprogram.payment.dto.CartItemResponse(
                ca.cartId,
                ca.course.courseId,
                ca.course.title,
                ca.course.price,
                ca.course.instructor.name
            )
            FROM Cart ca
            WHERE ca.member.memberId = :memberId
            ORDER BY ca.createdAt DESC
            """)
    List<CartItemResponse> findCartItemsByMemberId(@Param("memberId") Long memberId);

    @Query(value = """
            SELECT COUNT(*)
            FROM ENROLLMENT
            WHERE member_id = :memberId
              AND course_id  = :courseId
            """, nativeQuery = true)
    long countEnrollmentByMemberAndCourse(@Param("memberId") Long memberId,
                                          @Param("courseId") Long courseId);
}
