package com.somemore.global.exception.handler;

import com.somemore.global.auth.jwt.exception.JwtException;
import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.DuplicateException;
import com.somemore.global.exception.ImageUploadException;
import com.somemore.global.exception.InvalidAuthenticationException;
import com.somemore.global.exception.NoSuchElementException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //예시 코드
    @ExceptionHandler(BadRequestException.class)
    ProblemDetail handleBadRequestException(final BadRequestException e) {

        //status와 에러에 대한 자세한 설명
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        // 아래와 같이 필드 확장 가능
        problemDetail.setTitle("잘못된 요청입니다");

        return problemDetail;
    }

    @ExceptionHandler(ImageUploadException.class)
    ProblemDetail handleImageUploadException(final ImageUploadException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        problemDetail.setTitle("이미지 업로드 실패");
        problemDetail.setDetail("업로드 중 문제가 발생했습니다. 파일 크기나 형식이 올바른지 확인해 주세요.");

        return problemDetail;
    }

    @ExceptionHandler(DuplicateException.class)
    ProblemDetail handleDuplicateException(final DuplicateException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("중복 예외");

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail handleMethodArgumentNotValid(final MethodArgumentNotValidException e) {

        String errorMessage = e.getBindingResult().getAllErrors().getFirst().getDefaultMessage();
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errorMessage);

        problemDetail.setTitle("유효성 예외");

        return problemDetail;
    }

    @ExceptionHandler(NoSuchElementException.class)
    ProblemDetail handleNoSuchElementException(final NoSuchElementException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("데이터가 존재하지 않음");

        return problemDetail;
    }

    @ExceptionHandler(InvalidAuthenticationException.class)
    ProblemDetail handleInvalidAuthenticationException(InvalidAuthenticationException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("인증 문제");
        problemDetail.setDetail("인증에 문제가 발생했습니다.");

        log.warn("InvalidAuthenticationException: {}", e.getMessage());

        return problemDetail;
    }

    @ExceptionHandler(JwtException.class)
    ProblemDetail handleJwtException(JwtException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("토큰 문제");
        problemDetail.setDetail("토큰 처리에 문제가 발생했습니다.");

        log.warn("JwtException: {}", e.getMessage());

        return problemDetail;
    }

}
