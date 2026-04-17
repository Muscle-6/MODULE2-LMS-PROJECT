package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminReportListResponse;
import com.wanted.ailienlmsprogram.admin.repository.AdminReportQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*관리자 신고 관리 기능의 조회 로직을 담당하는 서비스.*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final AdminReportQueryRepository adminReportQueryRepository;

    public List<AdminReportListResponse> getReports() {
        return adminReportQueryRepository.findReports();
    }
}