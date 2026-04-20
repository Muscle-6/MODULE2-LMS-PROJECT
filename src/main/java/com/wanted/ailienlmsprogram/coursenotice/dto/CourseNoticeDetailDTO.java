package com.wanted.ailienlmsprogram.coursenotice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CourseNoticeDetailDTO {

    private Long noticeId;
    private Long courseID;
    private String noticeTitle;
    private String noticeContent;
    private String authorName;      // author.getName() 으로 세팅
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
