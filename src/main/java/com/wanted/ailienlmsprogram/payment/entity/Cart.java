package com.wanted.ailienlmsprogram.payment.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "CART")
@Getter
@NoArgsConstructor
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    // 생성 메서드
    public static Cart create(Course course, Member member) {
        Cart cart = new Cart();
        cart.course = course;
        cart.member = member;
        cart.createdAt = LocalDateTime.now();
        return cart;
    }
}
