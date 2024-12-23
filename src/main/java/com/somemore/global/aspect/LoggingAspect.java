package com.somemore.global.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Pointcut("execution(* com.somemore.domains.*.controller..*.*(..))")
    private void pointCut(){}

    @Around("pointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {

        String methodName = joinPoint.getSignature().toShortString();
        String args = convertArgsToJson(joinPoint.getArgs());
        log.info("엔드포인트 호출: {} \n- 파라미터: {}", methodName, args);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;

            log.debug("성공: {} \n- 응답: {} \n- 실행 시간: {}ms",
                    methodName,
                    convertResultToJson(result),
                    elapsedTime);

            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;

            log.warn("에러 발생: {} \n- 에러 타입: {} \n- 에러 메세지: {} \n- 실행 시간: {}ms",
                    methodName,
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    elapsedTime);

            throw e;
        }
    }

    private String convertArgsToJson(Object[] args) {
        try {
            return objectMapper.writeValueAsString(args != null ? args : new Object[]{});
        } catch (Exception e) {
            log.warn("파라미터 변환 실패", e);
            return "[파라미터 변환 실패]";
        }
    }

    private String convertResultToJson(Object result) {
        try {
            return objectMapper.writeValueAsString(result != null ? result : "null");
        } catch (Exception e) {
            log.warn("응답 변환 실패", e);
            return "[응답 변환 실패]";
        }
    }
}
