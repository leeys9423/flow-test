package com.flow.domain.extension.entities;

import com.flow.domain.extension.exception.ExtensionException;
import com.flow.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fixed_extension")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class FixedExtension extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "extension_lower", length = 10, nullable = false, unique = true)
    private String extensionLower;

    @Column(name = "extension_upper", length = 10, nullable = false)
    private String extensionUpper;

    @Column(nullable = false)
    private boolean blocked = false;

    @Builder
    public FixedExtension(String extension, Boolean blocked) {
        setExtension(extension);
        this.blocked = blocked != null ? blocked : false;
    }

    private void setExtension(String extension) {
        validate(extension);

        String cleanExt = extension.trim().toLowerCase();
        this.extensionLower = cleanExt;
        this.extensionUpper = cleanExt.toUpperCase();
    }

    private void validate(String extension) {
        if (extension == null || extension.trim().isEmpty()) {
            throw ExtensionException.invalidFormat();
        }
    }

    public void toggleBlocked() {
        this.blocked = !this.blocked;
    }
}
