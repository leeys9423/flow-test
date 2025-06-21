package com.flow.domain.extension.service;

import com.flow.domain.extension.dto.response.ExtensionResponse;
import com.flow.domain.extension.entities.CustomExtension;
import com.flow.domain.extension.exception.ExtensionException;
import com.flow.domain.extension.repository.CustomExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomExtensionService {

    private final CustomExtensionRepository customExtensionRepository;
    private final FixedExtensionService fixedExtensionService;
    private static final int MAX_CUSTOM_EXTENSIONS = 200;

    /**
     * 모든 커스텀 확장자 조회 (응답 DTO)
     */
    public List<ExtensionResponse> findAllAsResponse() {
        return customExtensionRepository.findAll().stream()
                .map(ExtensionResponse::fromCustom)
                .toList();
    }

    /**
     * 커스텀 확장자 추가 (엔티티 반환)
     */
    @Transactional
    public CustomExtension addExtension(String extension) {
        // 1. 최대 개수 체크 (200개)
        if (customExtensionRepository.count() >= MAX_CUSTOM_EXTENSIONS) {
            throw ExtensionException.maxExceeded();
        }

        // 2. 중복 체크 (대소문자 무관)
        String lowerExt = extension.trim().toLowerCase();
        if (customExtensionRepository.existsByExtensionLower(lowerExt)) {
            throw ExtensionException.duplicate(extension);
        }

        if (fixedExtensionService.isFixedExtension(lowerExt)) {
            throw ExtensionException.duplicateWithFixed(extension);
        }

        // 3. 저장
        CustomExtension customExt = CustomExtension.builder()
                .extension(extension)
                .build();

        return customExtensionRepository.save(customExt);
    }

    /**
     * 커스텀 확장자 추가 (응답 DTO 반환) - API용
     */
    @Transactional
    public ExtensionResponse addExtensionAsResponse(String extension) {
        CustomExtension savedExtension = addExtension(extension);
        return ExtensionResponse.fromCustom(savedExtension);
    }

    /**
     * 커스텀 확장자 삭제
     */
    @Transactional
    public void deleteExtension(Long id) {
        CustomExtension extension = customExtensionRepository.findById(id)
                .orElseThrow(ExtensionException::notFound);

        customExtensionRepository.delete(extension);
    }
}
