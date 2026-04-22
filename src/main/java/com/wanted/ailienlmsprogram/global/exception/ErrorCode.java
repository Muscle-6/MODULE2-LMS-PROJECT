package com.wanted.ailienlmsprogram.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 전역 예외 처리 시 사용할 최소 상태코드 정의.
 * 현재 프로젝트는 화면 반환 중심이므로
 * 상태코드, 제목, 메시지, 이동할 뷰 이름만 관리한다.
 */
@Getter
public enum ErrorCode {

    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청", "요청 정보를 다시 확인해주세요.", "error/400"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한 없음", "해당 구역에 접근할 권한이 없습니다.", "common/access-denied"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "페이지를 찾을 수 없음", "요청한 페이지 또는 데이터를 찾을 수 없습니다.", "error/404"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 요청 방식", "현재 요청 방식으로는 접근할 수 없습니다.", "error/405"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류", "서버 내부 오류가 발생했습니다.", "error/500");

    private final HttpStatus status;
    private final String title;
    private final String message;
    private final String viewName;

    ErrorCode(HttpStatus status, String title, String message, String viewName) {
        this.status = status;
        this.title = title;
        this.message = message;
        this.viewName = viewName;
    }
}