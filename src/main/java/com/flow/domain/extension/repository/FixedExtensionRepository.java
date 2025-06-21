package com.flow.domain.extension.repository;

import com.flow.domain.extension.entities.FixedExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FixedExtensionRepository extends JpaRepository<FixedExtension, Long> {
    boolean existsByExtensionLower(String extensionLower);
}
