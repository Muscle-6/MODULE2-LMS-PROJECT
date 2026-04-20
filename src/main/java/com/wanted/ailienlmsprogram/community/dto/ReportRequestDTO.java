package com.wanted.ailienlmsprogram.community.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportRequestDTO {
    private String targetType;
    private Long targetNo;
    private String reason;
}