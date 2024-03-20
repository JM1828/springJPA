package com.example.firstproject.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// @RunningTime 어노테이션을 적용할 수 있는 대상을 지정하는 부분
// 여기서는 클래스와 메서드에 어노테이션을 적용할 수 있도록 설정되어 있음
@Target({ElementType.TYPE, ElementType.METHOD}) // 어노테이션 적용 대상
// @RunningTime 어노테이션의 유지 시간을 설정하는 부분
@Retention(RetentionPolicy.RUNTIME) // 어노테이션 유지 시간
public @interface RunningTime {
}
