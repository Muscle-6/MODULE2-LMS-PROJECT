package com.wanted.ailienlmsprogram.member.service;

import com.wanted.ailienlmsprogram.member.dto.SignupRequest;
import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;

/*회원가입 로그인 후 회원 상태 갱신을 담당하는 서비스*/
@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    /*신규 학생 회원가입 처리
    *
    * 1. loginId 중복 체크
    * 2. email 중복 체크
    * 3. password 암호화
    * 4. 기본 역할(STUDENT), 계정 상태(Active), 초기 등급(Reptilian) 설정*/
    @Transactional
    public void signup(SignupRequest request) {
        if (memberRepository.existsByLoginId(request.getLoginId())) {
            throw new IllegalArgumentException("이미 사용 중인 아이디입니다.");
        }

        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
        }

        Member member = new Member();
        member.setLoginId(request.getLoginId());
        member.setEmail(request.getEmail());
        member.setPassword(passwordEncoder.encode(request.getPassword()));
        member.setName(request.getName());
        member.setPhone(request.getPhone());
        member.setRole(Member.MemberRole.STUDENT);
        member.setAccountStatus(Member.AccountStatus.ACTIVE);

        // 첫 회원가입 등급
        member.setRank(Member.MemberRank.REPTILIAN);

        LocalDateTime now = LocalDateTime.now();
        member.setCreatedAt(now);
        member.setUpdatedAt(now);

        memberRepository.save(member);
    }

    /*로그인 성공 직후 회원 정보를 갱신한다.
    *
    * 1. 회원 조회
    * 2. 마지막 로그인 시점 기준 등급 강등 정책 적용
    * 3. lastLoginAt 갱신
    * 4. updatedAt 갱신*/
    @Transactional
    public void handleLoginSuccess(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("회원이 존재하지 않습니다."));

        LocalDateTime now = LocalDateTime.now();

        // 로그인 시점에는 승급 없음, 강등만 판단
        applyRankPolicy(member, now);

        // 다음 로그인 기준 시점 갱신
        member.setLastLoginAt(now);
        member.setUpdatedAt(now);
    }

    // 마지막 로그인 시점과 현재 시각 차이를 기준으로 등급 정책을 적용한다.
    private void applyRankPolicy(Member member, LocalDateTime now) {
        LocalDateTime lastLoginAt = member.getLastLoginAt();

        // 첫 로그인이라면 강등 없이 현재 등급 유지
        if (lastLoginAt == null) {
            return;
        }

        long inactiveDays = Duration.between(lastLoginAt, now).toDays();

        // 7일 이상 미로그인 -> NOVICE
        if (inactiveDays >= 7) {
            member.setRank(Member.MemberRank.NOVICE);
            return;
        }

        // 3일 이상 7일 미만 미로그인 -> MINERVAL (REPTILIAN 등급에만 적용)
        if (inactiveDays >= 3 && member.getRank() == Member.MemberRank.REPTILIAN) {
            member.setRank(Member.MemberRank.MINERVAL);
        }
    }
}