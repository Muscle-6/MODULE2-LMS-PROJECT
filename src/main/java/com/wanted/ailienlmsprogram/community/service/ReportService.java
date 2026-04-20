package com.wanted.ailienlmsprogram.community.service;

import com.wanted.ailienlmsprogram.community.entity.Report;
import com.wanted.ailienlmsprogram.community.entity.TargetType;
import com.wanted.ailienlmsprogram.community.repository.ReportRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReportService {

    private final ReportRepository reportRepository;

    public void createReport(Long reporterNo, String targetType, Long targetNo, String reason) {
        Report report = Report.create(reporterNo, TargetType.valueOf(targetType), targetNo, reason);

        reportRepository.save(report);
    }
}