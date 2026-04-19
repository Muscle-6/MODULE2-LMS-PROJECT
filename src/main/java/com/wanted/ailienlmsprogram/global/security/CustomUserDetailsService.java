package com.wanted.ailienlmsprogram.global.security;

import com.wanted.ailienlmsprogram.member.entity.Member;
import com.wanted.ailienlmsprogram.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

/*로그인 아이디(loginId)로 회원 정보를 조회하는 Spring Security 전용 서비스
*
* 전체 흐름: 사용자가 /login POST 요청
* - AuthenticationProvider가 이 서비스를 호출
* - DB에서 회원 조회
* - 조회된 Member를 CustomUserDetails로 변환해 반환*/
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    /*loginId로 회원을 조회해 userDetails로 반환한다.*/
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("존재하지 않는 아이디입니다."));

        return new CustomUserDetails(member);
    }
}