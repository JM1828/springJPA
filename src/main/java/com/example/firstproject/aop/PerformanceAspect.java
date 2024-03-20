package com.example.firstproject.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Aspect
@Component
@Slf4j
public class PerformanceAspect {

    // @annotation 표현식을 사용하여 특정 어노테이션을 대상으로 지정
    @Pointcut("@annotation(com.example.firstproject.annotation.RunningTime)") // 특정 어노테이션을 대상 지정
    private void enableRunningTime() {}

    // 특정 메서드 실행 지점을 지정하는 데 사용
    @Pointcut("execution(* com.example.firstproject..*.*(..))") // 기본 패키지의 모든 메소드
    private void cut() {}

    // 실행 시점 설정: 두 조건을 모두 만족하는 대상을 전후로 부가 기능을 삽입
    @Around("cut() && enableRunningTime()") // @Around 어드바이스는 대상 메서드 실행 이전과 이후에 추가적인 동작을 수행할 수 있음
    // ProceedingJoinPoint 는 대상 메서드의 실행을 제어할 수 있는 객체
    // Throwable 은 모든 예외 클래스의 최상위 클래스이며, 예외 처리의 기본 타입
    public void loggingRunningTime(ProceedingJoinPoint joinPoint) throws Throwable {
        // 메소드 수행 전, 측정 시작
        StopWatch stopWatch = new StopWatch(); // StopWatch 객체를 생성하고 시작 시간을 기록
        stopWatch.start();

        // 메소드를 수행
        Object returningObj = joinPoint.proceed(); // proceed() 메서드를 호출하는 것으로, 대상 메소드의 실행을 진행

        // 메소드 수행 후, 측정 종료 및 로깅
        stopWatch.stop(); // 메소드의 수행이 완료된 후에는 측정을 종료

        // JoinPoint 를 통해 현재 실행 중인 메서드의 이름을 가져오기
        String methodName = joinPoint.getSignature()
                .getName();

        // getTotalTimeSeconds()를 사용하여 총 수행 시간을 초 단위로 얻을 수 있음
        log.info("{}의 총 수행 시간 => {} sec", methodName, stopWatch.getTotalTimeSeconds());
    }
}
