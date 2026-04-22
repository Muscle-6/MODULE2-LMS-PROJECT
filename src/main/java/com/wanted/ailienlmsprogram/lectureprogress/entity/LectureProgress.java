package com.wanted.ailienlmsprogram.lectureprogress.entity;

import com.wanted.ailienlmsprogram.lecture.entity.Lecture;
import com.wanted.ailienlmsprogram.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
@Entity
@Table(name = "lecture_progress")
public class LectureProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long progressId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lecture_id")
    private Lecture lecture;

    @Column(name = "progress_is_completed")
    private Boolean progressIsCompleted;

    public void complete() {
        this.progressIsCompleted = true;
    }

    public static LectureProgress create(Member member, Lecture lecture) {
        LectureProgress progress = new LectureProgress();
        progress.member = member;
        progress.lecture = lecture;
        progress.progressIsCompleted = true;
        return progress;
    }
}
