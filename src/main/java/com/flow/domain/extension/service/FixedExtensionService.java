package com.flow.domain.extension.service;

import com.flow.domain.extension.dto.response.ExtensionResponse;
import com.flow.domain.extension.entities.FixedExtension;
import com.flow.domain.extension.exception.ExtensionException;
import com.flow.domain.extension.repository.FixedExtensionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FixedExtensionService {

    private final FixedExtensionRepository fixedExtensionRepository;

    /**
     * 모든 고정 확장자 조회 (응답 DTO)
     */
    public List<ExtensionResponse> findAllAsResponse() {
        return fixedExtensionRepository.findAll().stream()
                .map(ExtensionResponse::fromFixed)
                .toList();
    }

    /**
     * 특정 고정 확장자 차단 상태 토글
     * @return 변경된 후의 차단 상태 (true: 차단, false: 허용)
     */
    @Transactional
    public boolean toggleBlocked(Long id) {
        FixedExtension fixedExtension = fixedExtensionRepository.findById(id)
                .orElseThrow(ExtensionException::notFound);

        fixedExtension.toggleBlocked();
        fixedExtensionRepository.save(fixedExtension);

        return fixedExtension.isBlocked();  // 변경된 상태 반환
    }

    /**
     * 고정 확장자 존재 여부 확인 (커스텀 확장자 중복 체크용)
     */
    public boolean isFixedExtension(String extension) {
        String lowerExt = extension.trim().toLowerCase();
        return fixedExtensionRepository.existsByExtensionLower(lowerExt);
    }
}
