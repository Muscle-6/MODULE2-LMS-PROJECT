package com.wanted.ailienlmsprogram.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*로그인 실패 시 실패 사유를 분류해서 로그인 페이지로 다시 보내느 Handler*/
@Component
public class SecurityAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
                                        HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {

        String reason = "invalid";

        if (exception instanceof DisabledException) {
            reason = "inactive";
        } else if (exception instanceof LockedException) {
            reason = "banned";
        }

        response.sendRedirect("/login?error=true&reason=" + reason);
    }
}