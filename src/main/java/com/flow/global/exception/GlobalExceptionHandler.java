package com.flow.global.exception;

import com.flow.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business Exception: [{}] {}", e.getErrorCode().getCode(), e.getMessage());

        ApiResponse<Void> response = ApiResponse.of(null, e.getMessage());

        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(response);
    }

    // spring validation
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("Validation Exception: {}", e.getMessage());

        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        ApiResponse<Void> response = ApiResponse.of(null,
                errorMessage.isEmpty() ? "잘못된 입력값입니다." : errorMessage);

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Void>> handleIllegalArgumentException(IllegalArgumentException e) {
        log.warn("IllegalArgumentException: {}", e.getMessage());

        ApiResponse<Void> response = ApiResponse.of(null, e.getMessage());

        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiResponse<Void>> handleRuntimeException(RuntimeException e) {
        log.error("Runtime Exception: ", e);

        ApiResponse<Void> response = ApiResponse.of(null, "처리 중 오류가 발생했습니다.");

        return ResponseEntity.internalServerError().body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("Unexpected Exception: ", e);

        ApiResponse<Void> response = ApiResponse.of(null, "서버 내부 오류가 발생했습니다.");

        return ResponseEntity.internalServerError().body(response);
    }
}
