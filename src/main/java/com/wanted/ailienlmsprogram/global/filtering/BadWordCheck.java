package com.wanted.ailienlmsprogram.global.filtering;

/*비속어 검사가 필요한 서비스 메서드에 붙이는 마커 어노테이션
* -> AOP가 이 어노테이션을 보고 해당 메서드를 가로챔*/

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)//메서드에서만 사용 가능
@Retention(RetentionPolicy.RUNTIME)//런타임에도 어노테이션 정보 유지(AOP가 읽어야 한다!!)
public @interface BadWordCheck{
}

