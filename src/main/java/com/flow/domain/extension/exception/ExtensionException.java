package com.flow.domain.extension.exception;

import com.flow.global.exception.BusinessException;
import com.flow.global.exception.ErrorCode;

public class ExtensionException extends BusinessException {

    public ExtensionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public ExtensionException(ErrorCode errorCode, String customMessage) {
        super(errorCode, customMessage);
    }

    public ExtensionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public static ExtensionException notFound() {
        return new ExtensionException(ErrorCode.EXTENSION_NOT_FOUND);
    }

    public static ExtensionException duplicate(String extension) {
        return new ExtensionException(
                ErrorCode.DUPLICATE_EXTENSION,
                String.format("확장자 '%s'는 이미 등록되어 있습니다.", extension)
        );
    }

    public static ExtensionException invalidFormat(String extension) {
        return new ExtensionException(
                ErrorCode.INVALID_EXTENSION_FORMAT,
                String.format("'%s'는 유효하지 않은 확장자 형식입니다.", extension)
        );
    }

    public static ExtensionException invalidFormat() {
        return new ExtensionException(ErrorCode.INVALID_EXTENSION_FORMAT);
    }

    public static ExtensionException maxExceeded() {
        return new ExtensionException(ErrorCode.MAX_CUSTOM_EXTENSIONS_EXCEEDED);
    }

    public static ExtensionException notModifiable() {
        return new ExtensionException(ErrorCode.FIXED_EXTENSION_NOT_MODIFIABLE);
    }

    public static ExtensionException duplicateWithFixed(String extension) {
        return new ExtensionException(
                ErrorCode.DUPLICATE_EXTENSION,
                String.format("확장자 '%s'는 고정 확장자에 이미 포함되어 있습니다.", extension));
    }
}
