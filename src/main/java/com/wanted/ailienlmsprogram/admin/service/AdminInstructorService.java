package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminInstructorCreateRequest;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminInstructorService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

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

    private void validateDuplicate(AdminInstructorCreateRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }
    }
}