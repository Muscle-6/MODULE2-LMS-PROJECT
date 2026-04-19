package com.wanted.ailienlmsprogram.global.filtering;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

//Aspect 구현
/*
* 1. 이제 서비스 메서드에 @BadWordCheck가 붙으면 DTO 안의 문자열 필드를
* 검사하게 만든다.
* 2. 현재 프로젝트의 PostCreateRequest 같은 DTO를 그대로 검사하기 좋다.
* */
@Aspect
@Component
@RequiredArgsConstructor
public class BadWordAspect {

    private final BadWordFilter badWordFilter;

    @Around("@annotation(com.wanted.ailienlmsprogram.global.filtering.BadWordCheck)")
    public Object inspectBadWords(ProceedingJoinPoint joinPoint) throws Throwable {
        for (Object arg : joinPoint.getArgs()) {
            inspectArgument(arg);
        }

        return joinPoint.proceed();
    }

    private void inspectArgument(Object arg) throws IllegalAccessException {
        if (arg == null) {
            return;
        }

        if (arg instanceof String text) {
            badWordFilter.validate("text", text);
            return;
        }

        String className = arg.getClass().getName();
        if (!className.contains(".dto.")) {
            return;
        }

        for (Field field : arg.getClass().getDeclaredFields()) {
            field.setAccessible(true);

            Object value = field.get(arg);
            if (value instanceof String text) {
                badWordFilter.validate(field.getName(), text);
            }
        }
    }
}