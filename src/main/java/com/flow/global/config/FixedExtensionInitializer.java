package com.flow.global.config;

import com.flow.domain.extension.entities.FixedExtension;
import com.flow.domain.extension.repository.FixedExtensionRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FixedExtensionInitializer {

    private final FixedExtensionRepository fixedExtensionRepository;

    /**
     * 애플리케이션 시작 시 고정 확장자 데이터 초기화
     */
    @PostConstruct
    @Transactional
    public void initializeFixedExtensions() {
        // 이미 데이터가 있으면 스킵
        if (fixedExtensionRepository.count() > 0) {
            log.info("고정 확장자 데이터가 이미 존재합니다. 초기화를 건너뜁니다.");
            return;
        }

        // 초기 고정 확장자 목록
        List<String> defaultExtensions = Arrays.asList("bat", "cmd", "com", "cpl", "exe", "scr", "js");

        log.info("고정 확장자 데이터를 초기화합니다...");

        for (String extension : defaultExtensions) {
            FixedExtension fixedExtension = FixedExtension.builder()
                    .extension(extension)
                    .blocked(false)
                    .build();

            fixedExtensionRepository.save(fixedExtension);
        }

        log.info("고정 확장자 {}개가 초기화되었습니다.", defaultExtensions.size());
    }
}
