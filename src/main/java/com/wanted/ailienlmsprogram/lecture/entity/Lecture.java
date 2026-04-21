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

    public enum VideoStatus {
        NONE,       // 영상 없는 강의
        UPLOADING,  // 업로드 중
        READY       // 재생 가능
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lecture_id")
    private Long lectureId;

    @ManyToOne(fetch = FetchType.LAZY)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "video_status", nullable = false)
    private VideoStatus videoStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;


    // 비동기 GCS 업로드 완료 후 URL + 상태 업데이트
    public void updateVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
        this.videoStatus = VideoStatus.READY;
    }

    // 비동기 업로드 시작 시 상태를 UPLOADING으로 변경
    public void markAsUploading() {
        this.videoStatus = VideoStatus.UPLOADING;
    }

    public void editLectureInfo(String lectureTitle, String lectureDescription, String videoUrl) {
        this.lectureTitle = lectureTitle;
        this.lectureDescription = lectureDescription;
        if (videoUrl != null) {
            this.videoUrl = videoUrl;
        }
    }

    // 생성 메서드
    public static Lecture create(Course course, String lectureTitle, String lectureDescription,
                                 Integer lectureOrderIndex, String videoUrl) {
        Lecture lecture = new Lecture();
        lecture.course = course;
        lecture.lectureTitle = lectureTitle;
        lecture.lectureDescription = lectureDescription;
        lecture.lectureOrderIndex = lectureOrderIndex;
        lecture.videoUrl = videoUrl;
        lecture.videoStatus = (videoUrl != null) ? VideoStatus.READY : VideoStatus.NONE;
        lecture.createdAt = LocalDateTime.now();
        return lecture;
    }
}
