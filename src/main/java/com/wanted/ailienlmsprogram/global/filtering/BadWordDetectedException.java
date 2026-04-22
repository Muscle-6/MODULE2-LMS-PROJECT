package com.wanted.ailienlmsprogram.global.filtering;

//GlobalExceptionHandler에서 보내는 비속어 예외 메시지를 받는다.
public class BadWordDetectedException extends RuntimeException {
    public BadWordDetectedException(String message) {
        super(message);
    }
}