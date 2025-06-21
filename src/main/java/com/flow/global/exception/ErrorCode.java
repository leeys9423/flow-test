package com.flow.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // 공통 에러 코드
    INVALID_INPUT_VALUE("COM-001", HttpStatus.BAD_REQUEST, "잘못된 입력값입니다."),
    INTERNAL_SERVER_ERROR("COM-002", HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),

    // 파일 확장자 관련 에러 코드
    EXTENSION_NOT_FOUND("EXT-001", HttpStatus.NOT_FOUND, "해당 확장자를 찾을 수 없습니다."),
    DUPLICATE_EXTENSION("EXT-002", HttpStatus.CONFLICT, "이미 등록된 확장자입니다."),
    INVALID_EXTENSION_FORMAT("EXT-003", HttpStatus.BAD_REQUEST, "잘못된 확장자 형식입니다."),
    MAX_CUSTOM_EXTENSIONS_EXCEEDED("EXT-004", HttpStatus.BAD_REQUEST, "커스텀 확장자 최대 개수를 초과했습니다."),
    FIXED_EXTENSION_NOT_MODIFIABLE("EXT-005", HttpStatus.FORBIDDEN, "고정 확장자는 삭제할 수 없습니다."),
    ;

    private final String code;
    private final HttpStatus status;
    private final String message;
}
