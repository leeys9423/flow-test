package com.flow.domain.extension.entities;

import com.flow.domain.extension.exception.ExtensionException;
import com.flow.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "custom_extension")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CustomExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "extension_lower", length = 20, nullable = false, unique = true)
    private String extensionLower;

    @Column(name = "extension_upper", length = 20, nullable = false)
    private String extensionUpper;

    @Builder
    public CustomExtension(String extension) {
        setExtension(extension);
    }

    private void setExtension(String extension) {
        String cleanExt = validate(extension);

        this.extensionLower = cleanExt;
        this.extensionUpper = cleanExt.toUpperCase();
    }

    private String validate(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            throw ExtensionException.invalidFormat();
        }

        String cleanExt = extension.trim().toLowerCase();
        if (!cleanExt.matches("^[a-z0-9]+$")) {  // 영숫자만 허용
            throw ExtensionException.invalidFormat(extension);
        }

        return cleanExt;
    }
}
