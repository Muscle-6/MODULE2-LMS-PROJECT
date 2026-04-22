package com.wanted.ailienlmsprogram.admin.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

/*통계 화면의 대륙 선택 드롭다운 한 줄을 표현하는 DTO.*/
@Getter
@AllArgsConstructor
public class AdminContinentOptionResponse {

    private Long continentId;
    private String continentName;
}