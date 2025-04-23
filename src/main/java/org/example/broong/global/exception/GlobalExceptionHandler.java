package org.example.broong.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 모든 예외를 하나로 처리
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<String> handleWriterException(ApiException e) {
        log.error("ApiException occurred. type={} message={} className={}", e.getErrorType(),
            e.getMessage(),
            e.getClass().getName());
        return ResponseEntity.status(e.getHttpStatus()).body(e.getMessage());
    }
}
