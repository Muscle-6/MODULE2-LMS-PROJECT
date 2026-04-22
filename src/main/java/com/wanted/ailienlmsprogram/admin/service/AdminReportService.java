package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminReportDetailResponse;
import com.wanted.ailienlmsprogram.admin.dto.AdminReportListResponse;
import com.wanted.ailienlmsprogram.admin.repository.AdminReportCommandRepository;
import com.wanted.ailienlmsprogram.admin.repository.AdminReportQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/*관리자 신고 관리 기능의 조회 로직을 담당하는 서비스.
* 신고 목록 조회 요청을 받고 조회 전용 Repository로 위임한다.*/
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminReportService {

    private final AdminReportQueryRepository adminReportQueryRepository;
    private final AdminReportCommandRepository adminReportCommandRepository;


    public List<AdminReportListResponse> getReports() {
        return adminReportQueryRepository.findReports();
    }


    public AdminReportDetailResponse getReportDetail(long reportId){
        return adminReportQueryRepository.findReportDetail(reportId);}

    @Transactional
    public void deleteReportedTarget(Long reportId){
        adminReportCommandRepository.deleteReportedTarget(reportId);
    }
}