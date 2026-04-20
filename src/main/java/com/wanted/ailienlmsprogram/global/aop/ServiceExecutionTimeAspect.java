package com.wanted.ailienlmsprogram.global.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ServiceExecutionTimeAspect {

    @Around("execution(* com.wanted.ailienlmsprogram..service..*(..))")
    public Object measureServiceExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {

        long start = System.currentTimeMillis();

        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = signature.getDeclaringType().getSimpleName();
        String methodName = signature.getName();

        try {
            Object result = joinPoint.proceed();

            long end = System.currentTimeMillis();
            System.out.println("[SERVICE TIME] " + className + "." + methodName
                    + "() 실행 시간 = " + (end - start) + "ms");

            return result;
        } catch (Throwable e) {
            long end = System.currentTimeMillis();
            System.out.println("[SERVICE TIME] " + className + "." + methodName
                    + "() 예외 발생, 실행 시간 = " + (end - start) + "ms");

            throw e;
        }
    }
}