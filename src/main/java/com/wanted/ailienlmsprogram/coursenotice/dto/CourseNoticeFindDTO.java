package com.wanted.ailienlmsprogram.coursenotice.dto;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CourseNoticeFindDTO {

    private Long noticeId;
    private String noticeTitle;
    private String noticeContent;
    private String authorName;      // author.getName() 으로 세팅
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}
