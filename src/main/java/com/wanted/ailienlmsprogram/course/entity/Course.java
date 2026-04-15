package com.wanted.ailienlmsprogram.course.entity;

import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "COURSE")
@Getter
@Setter
@NoArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @Column(name = "course_title", nullable = false, length = 200)
    private String title;

    @Column(name = "course_price")
    private Integer price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "instructor_id")
    private Member instructor;

    @Column(name = "continent_id")
    private Long continentId;

    @Column(name = "course_status", length = 20)
    private String status;

    @Column(name = "created_at")
    private LocalDateTime createdAt;
}