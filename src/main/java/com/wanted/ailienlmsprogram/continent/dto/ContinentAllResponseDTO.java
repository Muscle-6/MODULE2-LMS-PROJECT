package com.wanted.ailienlmsprogram.continent.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ContinentAllResponseDTO {

    private Long continentId;
    private String continentName;
    private String continentDescription;
    private String thumbnailUrl;

}
