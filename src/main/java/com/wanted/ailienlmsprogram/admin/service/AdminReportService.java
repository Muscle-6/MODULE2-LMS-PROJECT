package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminReportListResponse;
import com.wanted.ailienlmsprogram.admin.repository.AdminReportQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final AdminReportQueryRepository adminReportQueryRepository;

    public List<AdminReportListResponse> getReports() {
        return adminReportQueryRepository.findReports();
    }
}