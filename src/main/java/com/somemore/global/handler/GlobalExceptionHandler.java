package com.somemore.global.handler;

import com.somemore.global.exception.BadRequestException;
import com.somemore.global.exception.DuplicateException;
import com.somemore.global.exception.ImageUploadException;
import com.somemore.global.exception.NoSuchElementException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
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

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());

        problemDetail.setTitle("유효성 예외");
        problemDetail.setDetail("입력 데이터 유효성 검사가 실패했습니다. 각 필드를 확인해주세요.");

        return problemDetail;
    }

    @ExceptionHandler(NoSuchElementException.class)
    ProblemDetail handleNoSuchElementException(final NoSuchElementException e) {

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, e.getMessage());
        problemDetail.setTitle("데이터가 존재하지 않음");

        return problemDetail;
    }

}
