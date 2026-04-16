package com.wanted.ailienlmsprogram.admin.dto;

import lombok.Getter;

@Getter
public class AdminContinentNoticeWritePageResponse {

    private final Long continentId;
    private final String continentName;
    private final String description;

    public AdminContinentNoticeWritePageResponse(Long continentId,
                                                 String continentName,
                                                 String description) {
        this.continentId = continentId;
        this.continentName = continentName;
        this.description = description;
    }
}