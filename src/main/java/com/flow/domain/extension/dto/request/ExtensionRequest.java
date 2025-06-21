package com.flow.domain.extension.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ExtensionRequest {

    @NotBlank(message = "확장자는 필수입니다.")
    @Size(max = 10, message = "확장자는 10자 이하여야 합니다.")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "확장자는 영숫자만 가능합니다.")
    private String extension;
}
