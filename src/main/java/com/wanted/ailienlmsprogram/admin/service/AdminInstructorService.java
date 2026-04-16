package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminInstructorCreateRequest;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

/*관리자 강사 계정 생성 비즈니스 로직을 담당하는 서비스.
*
* - 강사 계정 생성 전 중복 아이디/이메일을 검증한다.
* - 비밀번호를 암호화하여 저장한다.
* - 생성되는 회원의 역할을 Instructor로 고정한다*/
@Service
@RequiredArgsConstructor
public class AdminInstructorService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    //강사 계정을 생성한다.
    @Transactional
    public void createInstructor(AdminInstructorCreateRequest request) {
        validateDuplicate(request);

        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());

        member.setRole(Member.MemberRole.INSTRUCTOR);
        member.setAccountStatus(Member.AccountStatus.ACTIVE);

        member.setCreatedAt(LocalDateTime.now());
        member.setUpdatedAt(LocalDateTime.now());

        // nullable 컬럼은 세팅하지 않음
        // phone, profileImageUrl, introduction, deletedAt, rank, lastLoginAt

        memberRepository.save(member);
    }

    // 강사 생성 요청의 중복 데이터를 검증한다.
    private void validateDuplicate(AdminInstructorCreateRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}