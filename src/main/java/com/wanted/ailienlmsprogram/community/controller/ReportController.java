package com.wanted.ailienlmsprogram.community.controller;

import com.wanted.ailienlmsprogram.community.dto.ReportRequestDTO;
import com.wanted.ailienlmsprogram.community.service.ReportService;
import com.wanted.ailienlmsprogram.global.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @PostMapping
    public ResponseEntity<String> submitReport(
            @RequestBody ReportRequestDTO request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {

        Long reporterNo = userDetails.getMember().getMemberId();
        reportService.createReport(
                reporterNo,
                request.getTargetType(),
                request.getTargetNo(),
                request.getReason()
        );

        return ResponseEntity.ok("신고가 정상적으로 접수되었습니다.");
    }
}