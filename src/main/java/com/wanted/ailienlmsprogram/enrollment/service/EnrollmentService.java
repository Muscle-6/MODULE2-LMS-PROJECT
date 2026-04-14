package com.wanted.ailienlmsprogram.enrollment.service;

import com.wanted.ailienlmsprogram.course.entity.Course;
import com.wanted.ailienlmsprogram.member.entity.Member;
import org.springframework.stereotype.Service;

/**
 * 수강 등록 서비스 — 현재 stub 상태.
 * 결제 완료 시 PaymentService에서 호출하며, 실제 ENROLLMENT 테이블 삽입 로직은 추후 구현한다.
 */
@Service
public class EnrollmentService {

    // TODO: EnrollmentRepository 주입 후 실제 ENROLLMENT 레코드 삽입 구현
    public void enroll(Member member, Course course) {
        // TODO: implement
    }
}
