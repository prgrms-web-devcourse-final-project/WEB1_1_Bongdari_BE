package com.somemore.global.aspect.log;

import com.somemore.global.aspect.log.extractor.ParameterExtractor;
import com.somemore.global.aspect.log.extractor.RequestExtractor;
import com.somemore.global.aspect.log.extractor.ResponseExtractor;
import com.somemore.global.common.response.LoggedResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.MDC;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
@Aspect
@Component
public class LoggingAspect {

    private final RequestExtractor requestExtractor;
    private final ResponseExtractor responseExtractor;
    private final ParameterExtractor parameterExtractor;

    @Pointcut("execution(* com.somemore.domains.*.controller..*.*(..))")
    private void controllerPointCut() {}

    @Around("controllerPointCut()")
    public Object logAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        MDC.put("requestId", requestId);

        try {
            return doLogAround(joinPoint);
        } finally {
            MDC.remove("requestId");
        }
    }

    private Object doLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().toShortString();
        HttpServletRequest request = requestExtractor.getCurrentRequest();

        MDC.put("method", request.getMethod());
        MDC.put("uri", request.getRequestURI());

        String params = parameterExtractor.extractParameters(joinPoint);
        log.info("엔드포인트 호출: {} \n- URI: {} \n- Method: {} \n- 파라미터: {}",
                methodName,
                request.getRequestURI(),
                request.getMethod(),
                params);

        long startTime = System.currentTimeMillis();

        try {
            Object result = joinPoint.proceed();
            long elapsedTime = System.currentTimeMillis() - startTime;

            LoggedResponse loggedResponse = responseExtractor.extractResponse(result);
            log.info("호출 성공: {} \n- 응답 코드: {} \n- 응답 값: {} \n- 실행 시간: {}ms",
                    methodName,
                    loggedResponse.getStatusCode(),
                    loggedResponse.getBody(),
                    elapsedTime);

            return result;
        } catch (Exception e) {
            long elapsedTime = System.currentTimeMillis() - startTime;
            HttpStatus status = responseExtractor.extractExceptionStatus(e);

            log.warn("예외 발생: {} \n- 예외 코드: {} \n- 예외 타입: {} \n- 예외 메세지: {} \n- 실행 시간: {}ms",
                    methodName,
                    status,
                    e.getClass().getSimpleName(),
                    e.getMessage(),
                    elapsedTime);

            throw e;
        } finally {
            MDC.clear();
        }
    }
}
