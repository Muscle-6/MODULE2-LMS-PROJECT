package com.wanted.ailienlmsprogram.enrollments.service;

import com.wanted.ailienlmsprogram.enrollments.dto.EnrollmentsDTO;
import com.wanted.ailienlmsprogram.enrollments.repository.EnrollmentsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EnrollmentsService {

    private final EnrollmentsRepository enrollmentsRepository;

    public EnrollmentsDTO findMyEnrollments(Long memberId) {

        return null;
    }
}
