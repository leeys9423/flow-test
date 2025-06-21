package com.flow.domain.extension.dto.response;

import com.flow.domain.extension.entities.CustomExtension;
import com.flow.domain.extension.entities.FixedExtension;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExtensionResponse {

    private Long id;
    private String extensionLower;
    private String extensionUpper;
    private boolean blocked;

    public static ExtensionResponse fromFixed(FixedExtension fixedExtension) {
        return new ExtensionResponse(
                fixedExtension.getId(),
                fixedExtension.getExtensionLower(),
                fixedExtension.getExtensionUpper(),
                fixedExtension.isBlocked()
        );
    }

    public static ExtensionResponse fromCustom(CustomExtension customExtension) {
        return new ExtensionResponse(
                customExtension.getId(),
                customExtension.getExtensionLower(),
                customExtension.getExtensionUpper(),
                true  // 커스텀은 항상 차단
        );
    }
}
