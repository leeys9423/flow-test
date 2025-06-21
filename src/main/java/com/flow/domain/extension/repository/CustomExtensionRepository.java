package com.flow.domain.extension.repository;

import com.flow.domain.extension.entities.CustomExtension;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomExtensionRepository extends JpaRepository<CustomExtension, Long> {
    boolean existsByExtensionLower(String extensionLower);
}
