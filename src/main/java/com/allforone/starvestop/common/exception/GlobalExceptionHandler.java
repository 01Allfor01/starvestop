package com.allforone.starvestop.common.exception;

import com.allforone.starvestop.common.dto.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CommonResponse<Void>> handleException(CustomException e) {
        log.error("예외 발생. ", e);

        CommonResponse<Void> response = CommonResponse.exception(e.getMessage());

        return ResponseEntity.status(e.getErrorCode().getStatus()).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<CommonResponse<Void>> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("예외 발생. ", e);

        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

        CommonResponse<Void> response = CommonResponse.exception(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<CommonResponse<Void>> IllegalArgumentException(IllegalArgumentException e) {
        log.error("예외 발생. ", e);

        String message = e.getMessage();
        CommonResponse<Void> response = CommonResponse.exception(message);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
}
