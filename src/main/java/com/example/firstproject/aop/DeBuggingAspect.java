package com.example.firstproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Aspect // 부가 기능 주입을 위한 AOP 클래스 선언
@Component  // IoC 컨테이너가 해당 객체를 생성 및 관리
@Slf4j
public class DeBuggingAspect {

    // 대상 메서드 선택: api 패키지 의 모든 메서드
    @Pointcut("execution(* com.example.firstproject.api.*.*(..))")
    private void cut() {}

    // 실행 시점 설정: cut()의 대상이 수행되기 이전
    @Before("cut()")
    public void loggingArgs(JoinPoint joinPoint) { // JoinPoint 는 메서드 실행, 메서드 호출, 필드 접근 등과 같은 애플리케이션 실행 중의 특정 지점을 의미
        // JoinPoint 를 통해 현재 실행 중인 메서드에 전달된 인자들을 가져오기
        Object[] args = joinPoint.getArgs();

        // JoinPoint 를 통해 현재 실행 중인 메서드가 속한 클래스의 이름을 가져오기
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // JoinPoint 를 통해 현재 실행 중인 메서드의 이름을 가져오기
        String methodName = joinPoint.getSignature()
                .getName();

        // 입력값 로깅하기
        // CommentService#create()의 입력값 => 5
        // CommentService#create()의 입력값 => CommentDto(id=null, ...)

        for (Object obj : args) {
            // {}는  placeholder 로 사용되는데, 이를 통해 문자열 내에 변수 값을 동적으로 삽입할 수 있음
            // {} 안에 위치한 className, methodName, obj 는 각각 클래스 이름, 메서드 이름, 인자 값을 나타냄
            log.info("{}#{}의 입력값 => {}", className, methodName, obj);
        }
    }

    // 실행 시점 설정: cut()에 지정된 대상이 호출이 정상적으로 성공 후!
    @AfterReturning(value = "cut()", returning = "returnObj") // 반환값을 returnObj 라는 이름의 파라미터로 전달받을 수 있도록 설정
    public void loggingReturnValue(JoinPoint joinPoint, // JoinPoint 는 메서드 실행, 메서드 호출, 필드 접근 등과 같은 애플리케이션 실행 중의 특정 지점을 의미
                                   Object returnObj) {  // 리턴값

        // JoinPoint 를 통해 현재 실행 중인 메서드가 속한 클래스의 이름을 가져오기
        String className = joinPoint.getTarget()
                .getClass()
                .getSimpleName();

        // JoinPoint 를 통해 현재 실행 중인 메서드의 이름을 가져오기
        String methodName = joinPoint.getSignature()
                .getName();

        // 반환값 로깅
        // CommentService#create()의 입력값 => CommentDto(id=10, ...)
        log.info("{}#{}의 반환값 => {}", className, methodName, returnObj);

    }
}
