package com.wanted.ailienlmsprogram.enrollments.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "enrollment")
public class Enrollments {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long enrollmentId;


}
