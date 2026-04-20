package com.wanted.ailienlmsprogram.coursenotice.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "course_notice")
public class CourseNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_id")
    private Long noticeId;

    @ManyToOne
    @JoinColumn(name = "course_id")
    private Course course;

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Member author;

    @Column(name = "notice_title")
    private String noticeTitle;

    @Column(name = "notice_content")
    private String noticeContent;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    public void editNotice(String noticeTitle, String noticeContent) {
        this.noticeTitle = noticeTitle;
        this.noticeContent = noticeContent;
        this.updatedAt = LocalDateTime.now();
    }

    // 생성 메서드
    public static CourseNotice create(Course course, Member author, String noticeTitle, String noticeContent) {
        CourseNotice notice = new CourseNotice();
        notice.course = course;
        notice.author = author;
        notice.noticeTitle = noticeTitle;
        notice.noticeContent = noticeContent;
        notice.createdAt = LocalDateTime.now();
        notice.updatedAt = LocalDateTime.now();
        return notice;
    }
}
