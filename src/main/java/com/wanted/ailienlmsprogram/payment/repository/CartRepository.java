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
                c.courseId,
                c.title,
                c.price,
                instr.name
            )
            FROM Cart ca
            JOIN ca.course c
            LEFT JOIN c.instructor instr
            WHERE ca.member.memberId = :memberId
            ORDER BY ca.createdAt DESC
            """)
    List<CartItemResponse> findCartItemsByMemberId(@Param("memberId") Long memberId);

    @Query(value = """
            SELECT COUNT(*)
            FROM ENROLLMENT
            WHERE member_id        = :memberId
              AND course_id        = :courseId
              AND enrollment_status = 'ACTIVE'
            """, nativeQuery = true)
    long countEnrollmentByMemberAndCourse(@Param("memberId") Long memberId,
                                          @Param("courseId") Long courseId);
}
