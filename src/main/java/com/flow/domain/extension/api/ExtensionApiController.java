package com.flow.domain.extension.api;

import com.flow.domain.extension.dto.request.ExtensionRequest;
import com.flow.domain.extension.dto.response.ExtensionResponse;
import com.flow.domain.extension.service.CustomExtensionService;
import com.flow.domain.extension.service.FixedExtensionService;
import com.flow.global.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/extensions")
@RequiredArgsConstructor
public class ExtensionApiController {

    private final FixedExtensionService fixedExtensionService;
    private final CustomExtensionService customExtensionService;

    /**
     * 고정 확장자 목록 조회
     */
    @GetMapping("/fixed")
    public ResponseEntity<ApiResponse<List<ExtensionResponse>>> getFixedExtensions() {
        List<ExtensionResponse> extensions = fixedExtensionService.findAllAsResponse();
        return ResponseEntity.ok(ApiResponse.of(extensions, "고정 확장자 목록을 조회했습니다."));
    }

    /**
     * 커스텀 확장자 목록 조회
     */
    @GetMapping("/custom")
    public ResponseEntity<ApiResponse<List<ExtensionResponse>>> getCustomExtensions() {
        List<ExtensionResponse> extensions = customExtensionService.findAllAsResponse();
        return ResponseEntity.ok(ApiResponse.of(extensions, "커스텀 확장자 목록을 조회했습니다."));
    }

    /**
     * 고정 확장자 차단 상태 토글
     */
    @PatchMapping("/fixed/{id}/toggle")
    public ResponseEntity<ApiResponse<Boolean>> toggleFixedExtension(@PathVariable Long id) {
        boolean newBlockedStatus = fixedExtensionService.toggleBlocked(id);
        String message = newBlockedStatus ? "확장자가 차단되었습니다." : "확장자 차단이 해제되었습니다.";
        return ResponseEntity.ok(ApiResponse.of(newBlockedStatus, message));
    }

    /**
     * 커스텀 확장자 추가
     */
    @PostMapping("/custom")
    public ResponseEntity<ApiResponse<ExtensionResponse>> addCustomExtension(@Valid @RequestBody ExtensionRequest request) {
        ExtensionResponse response = customExtensionService.addExtensionAsResponse(request.getExtension());
        return ResponseEntity.ok(ApiResponse.of(response, "커스텀 확장자가 추가되었습니다."));
    }

    /**
     * 커스텀 확장자 삭제
     */
    @DeleteMapping("/custom/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteCustomExtension(@PathVariable Long id) {
        customExtensionService.deleteExtension(id);
        return ResponseEntity.ok(ApiResponse.of(null, "커스텀 확장자가 삭제되었습니다."));
    }
}
