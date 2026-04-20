package com.wanted.ailienlmsprogram.coursecommand.entity;

import com.wanted.ailienlmsprogram.continent.entity.Continent;
import com.wanted.ailienlmsprogram.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "COURSE")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long courseId;

    @ManyToOne
    @JoinColumn(name = "continent_id", nullable = false)
    private Continent continent;

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    private User instructor;

    @Column(name = "course_title", nullable = false, length = 200)
    private String courseTitle;

    @Column(name = "course_description", columnDefinition = "TEXT")
    private String courseDescription;

    @Column(name = "course_thumbnail_url", length = 500)
    private String courseThumbnailUrl;

    @Column(name = "course_price", nullable = false)
    private Integer coursePrice;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "course_status")
    private CourseStatus courseStatus;

    // 수정 메서드
    public Course editCourseInfo(String courseTitle, String courseDescription,
                                  String courseThumbnailUrl, Integer coursePrice,
                                  Continent continentId) {
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseThumbnailUrl = courseThumbnailUrl;
        this.coursePrice = coursePrice;
        this.continent = continentId;
        return this;
    }

    // 생성 메서드
    public static Course create(Continent continent, User instructor, String courseTitle,
                                String courseDescription, String courseThumbnailUrl, Integer coursePrice) {
        Course course = new Course();
        course.continent = continent;
        course.instructor = instructor;
        course.courseTitle = courseTitle;
        course.courseDescription = courseDescription;
        course.courseThumbnailUrl = courseThumbnailUrl;
        course.coursePrice = coursePrice;
        course.createdAt = LocalDateTime.now();
        course.courseStatus = CourseStatus.DRAFT;
        return course;
    }
}