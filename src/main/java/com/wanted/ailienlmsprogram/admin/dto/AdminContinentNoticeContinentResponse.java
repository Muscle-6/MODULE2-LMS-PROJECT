package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;

@Getter
public class AdminContinentNoticeContinentResponse {

    private final Long continentId;
    private final String continentName;
    private final String description;
    private final Long noticeCount;

    public AdminContinentNoticeContinentResponse(Long continentId,
                                                 String continentName,
                                                 String description,
                                                 Long noticeCount) {
        this.continentId = continentId;
        this.continentName = continentName;
        this.description = description;
        this.noticeCount = noticeCount;
    }
}