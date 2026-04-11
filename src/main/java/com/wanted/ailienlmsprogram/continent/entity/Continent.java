package com.wanted.ailienlmsprogram.continent.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "CONTINENT")
@Getter
@Setter
@NoArgsConstructor
public class Continent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "대륙번호")
    private Long continentId;

    @Column(name = "대륙명", nullable = false)
    private String continentName;

    @Column(name = "설명")
    private String description;

    @Column(name = "썸네일URL")
    private String thumbnailUrl;
}