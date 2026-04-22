package com.wanted.ailienlmsprogram.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/*비밀번호 암호화 전략을 Spring Bean으로 등록하는 설정 클래스.
*
* 전체 흐름:
* 회원가입 시 MemberService에서 raw password를 암호화 할 때 사용한다.
* 로그인 시 Spring Security의 AuthenticationProvider가
* 입력 비밀번호화 DB 암호화 비밀번호를 비교할 때 사용한다.*/
@Configuration
public class PasswordConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}