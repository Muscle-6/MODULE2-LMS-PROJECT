package com.wanted.ailienlmsprogram.global.exception;

import com.wanted.ailienlmsprogram.global.filtering.BadWordDetectedException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * 프로젝트 전역 예외 처리기.
 * 수업 자료의 GlobalExceptionHandler 흐름을 유지하되,
 * 현재 LMS 프로젝트에 맞게 상태코드별 페이지 반환을 추가한다.
 */
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadWordDetectedException.class)
    public String handleBadWordDetectedException(BadWordDetectedException e,
                                                 HttpServletRequest request,
                                                 RedirectAttributes redirectAttributes) {

        log.warn("[BadWordDetectedException] path={}, message={}",
                request.getRequestURI(), e.getMessage());

        redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());

        String referer = request.getHeader("Referer");
        if (referer != null && !referer.isBlank()) {
            return "redirect:" + referer;
        }

        return "redirect:/";
    }


    @ExceptionHandler(BusinessException.class)
    public String handleBusinessException(BusinessException e,
                                          HttpServletRequest request,
                                          HttpServletResponse response,
                                          Model model) {

        log.warn("[BusinessException] path={}, message={}", request.getRequestURI(), e.getMessage());

        ErrorCode errorCode = e.getErrorCode();
        response.setStatus(errorCode.getStatus().value());

        setErrorAttributes(model, request, errorCode.getStatus().value(), errorCode.getTitle(), e.getMessage());

        return errorCode.getViewName();
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgumentException(IllegalArgumentException e,
                                                 HttpServletRequest request,
                                                 HttpServletResponse response,
                                                 Model model) {

        log.warn("[IllegalArgumentException] path={}, message={}", request.getRequestURI(), e.getMessage());

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        response.setStatus(errorCode.getStatus().value());

        setErrorAttributes(model, request, errorCode.getStatus().value(), errorCode.getTitle(),
                safeMessage(e.getMessage(), errorCode.getMessage()));

        return errorCode.getViewName();
    }

    @ExceptionHandler(IllegalStateException.class)
    public String handleIllegalStateException(IllegalStateException e,
                                              HttpServletRequest request,
                                              HttpServletResponse response,
                                              Model model) {

        log.warn("[IllegalStateException] path={}, message={}", request.getRequestURI(), e.getMessage());

        ErrorCode errorCode = ErrorCode.BAD_REQUEST;
        response.setStatus(errorCode.getStatus().value());

        setErrorAttributes(model, request, errorCode.getStatus().value(), errorCode.getTitle(),
                safeMessage(e.getMessage(), errorCode.getMessage()));

        return errorCode.getViewName();
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public String handleMethodNotSupported(HttpRequestMethodNotSupportedException e,
                                           HttpServletRequest request,
                                           HttpServletResponse response,
                                           Model model) {

        log.warn("[MethodNotSupported] path={}, method={}, message={}",
                request.getRequestURI(), request.getMethod(), e.getMessage());

        ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED;
        response.setStatus(errorCode.getStatus().value());

        setErrorAttributes(model, request, errorCode.getStatus().value(), errorCode.getTitle(), errorCode.getMessage());

        return errorCode.getViewName();
    }

    /**
     * 마지막 기본 예외 처리.
     * 수업 자료의 Exception.class 기본 처리 흐름과 동일한 역할이다.
     */
    @ExceptionHandler(Exception.class)
    public String handleDefaultException(Exception e,
                                         HttpServletRequest request,
                                         HttpServletResponse response,
                                         Model model) {

        log.error("[DefaultException] path={}, message={}", request.getRequestURI(), e.getMessage(), e);

        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
        response.setStatus(errorCode.getStatus().value());

        setErrorAttributes(model, request, errorCode.getStatus().value(), errorCode.getTitle(), errorCode.getMessage());

        return errorCode.getViewName();
    }

    private void setErrorAttributes(Model model,
                                    HttpServletRequest request,
                                    int status,
                                    String title,
                                    String message) {
        model.addAttribute("status", status);
        model.addAttribute("title", title);
        model.addAttribute("message", message);
        model.addAttribute("path", request.getRequestURI());
    }

    private String safeMessage(String actualMessage, String defaultMessage) {
        return (actualMessage == null || actualMessage.isBlank()) ? defaultMessage : actualMessage;
    }
}