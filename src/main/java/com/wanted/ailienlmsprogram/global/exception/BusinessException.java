package com.wanted.ailienlmsprogram.global.exception;

import lombok.Getter;

/**
 * 서비스 계층에서 의미 있는 예외를 던질 때 사용하는 공통 예외.
 */
@Getter
public class BusinessException extends RuntimeException {

    private final ErrorCode errorCode;

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}