package com.wanted.ailienlmsprogram.admin.service;

import com.wanted.ailienlmsprogram.admin.dto.AdminInstructorCreateRequest;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import com.wanted.ailienlmsprogram.global.exception.BusinessException;
import com.wanted.ailienlmsprogram.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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

        Member member = Member.create(
                request.getLoginId(),
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getName(),
                null,
                Member.MemberRole.INSTRUCTOR,
                Member.AccountStatus.ACTIVE,
                null
        );

        memberRepository.save(member);
    }

    // 강사 생성 요청의 중복 데이터를 검증한다.
    private void validateDuplicate(AdminInstructorCreateRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(ErrorCode.BAD_REQUEST, "이미 사용 중인 이메일입니다.");
        }
    }
}