package com.wanted.ailienlmsprogram.global.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/*인증되지 않은 사용자가 보호된 URL에 접근했을 때 처리하는 EntityPoint*/
@Component
public class SecurityAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /*미인증 접근 시 호출되는 메서드.
    *
    * - AJAX 요청: 401 + JSON 메시지
    * - 일반 요청: /login?required=true&redirect = 현재주소로 이동*/
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        if (isAjaxRequest(request)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"error\":\"UNAUTHORIZED\",\"message\":\"로그인이 필요합니다.\"}");
            return;
        }

        String redirectUrl = request.getRequestURI();
        String encodedRedirectUrl = URLEncoder.encode(redirectUrl, StandardCharsets.UTF_8);
        response.sendRedirect("/login?required=true&redirect=" + encodedRedirectUrl);
    }

    /**
     * AJAX 요청 여부 판별.
     * X-Requested-With: XMLHttpRequest 헤더가 있거나
     * Accept / Content-Type 에 application/json이 포함된 경우를 AJAX로 간주한다.
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        String requestedWith = request.getHeader("X-Requested-With");
        if ("XMLHttpRequest".equalsIgnoreCase(requestedWith)) {
            return true;
        }

        String contentType = request.getContentType();
        if (contentType != null && contentType.contains("application/json")) {
            return true;
        }

        String accept = request.getHeader("Accept");
        return accept != null && accept.contains("application/json");
    }
}
