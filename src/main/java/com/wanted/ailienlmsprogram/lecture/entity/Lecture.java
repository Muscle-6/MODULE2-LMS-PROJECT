package com.wanted.ailienlmsprogram.lecture.entity;

import com.wanted.ailienlmsprogram.coursecommand.entity.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity(name = "lecture")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    @ManyToOne
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "lecture_title")
    private String lectureTitle;

    @Column(name = "lecture_description")
    private String lectureDescription;

    @Column(name = "lecture_order_index")
    private Integer lectureOrderIndex;

    @Column(name = "video_url")
    private String videoUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    public void editLectureInfo(String lectureTitle, String lectureDescription, String videoUrl) {
        this.lectureTitle = lectureTitle;
        this.lectureDescription = lectureDescription;
        if (videoUrl != null) {          // 새 영상 업로드 했을 때만 교체
            this.videoUrl = videoUrl;
        }
    }

}
