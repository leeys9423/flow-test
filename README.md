# 파일 확장자 차단 기능

## 📋 기능 개요
파일 확장자에 따라 특정 형식의 파일을 첨부하거나 전송하지 못하도록 제한하는 기능

## 🎯 요구사항
1. **고정 확장자**: 자주 차단하는 확장자 목록 (기본값: 체크 해제)
2. **커스텀 확장자**: 사용자가 직접 추가/삭제 가능한 확장자 (최대 200개, 최대 20자리)

## 🤔 설계 고민과 해결 방안

### 1. 아키텍처 구조
**고민**: 전통적인 MVC vs 모던한 API 구조 중 선택

**해결**:
- `WebController`: 첫 화면만 렌더링 (Thymeleaf)
- `ApiController`: 모든 CRUD 작업을 JSON API로 처리
- `JavaScript`: 사용자 인터랙션과 API 호출 담당

```
WebController → HTML 페이지 렌더링
JavaScript → API 호출로 동적 업데이트  
ApiController → JSON 응답
```

**장점**: SPA-like 사용자 경험 + API 재사용성

### 2. 정적 리소스 분리
**고민**: HTML 내 인라인 스타일/스크립트 vs 별도 파일

**해결**: 완전 분리 구조
```
templates/extensions.html    → 구조만
static/css/extensions.css    → 스타일만
static/js/extensions.js      → 로직만
```

**장점**: 유지보수성, 캐싱 효과, 협업 효율성

### 3. 서비스 레이어 설계
**고민**: Repository 직접 주입 vs Service 간 소통

**해결**: Service → Service 구조
```java
// CustomExtensionService
private final FixedExtensionService fixedExtensionService;  // ✅

if (fixedExtensionService.isFixedExtension(extension)) {   // ✅
    throw ExtensionException.duplicateWithFixed(extension);
}
```

**장점**: 레이어 분리, 캡슐화, 재사용성

### 4. 중복 체크 로직
**고민**: 어느 범위까지 중복을 체크할 것인가?

**해결**: 3단계 검증
1. 최대 개수 체크 (200개)
2. **고정 확장자와 중복 체크** ← 핵심!
3. 커스텀 확장자 내 중복 체크

```java
// 예시: 'exe' 추가 시도
if (fixedExtensionService.isFixedExtension("exe")) {
    throw ExtensionException.duplicateWithFixed("exe");
    // → "확장자 'exe'는 고정 확장자에 이미 포함되어 있습니다."
}
```

### 5. 대소문자 처리
**고민**: PDF와 pdf를 어떻게 처리할 것인가?

**해결**: 내부적으로 소문자 통일, 사용자에게는 양쪽 표시
```java
// 저장 시
this.extensionLower = extension.trim().toLowerCase();  // pdf
this.extensionUpper = cleanExt.toUpperCase();          // PDF

// 화면 표시: pdf (소문자로 통일)
// API 응답: both lower/upper 제공
```

### 6. 데이터 초기화
**고민**: 고정 확장자 데이터를 어떻게 초기화할 것인가?

**해결**: `@PostConstruct` 방식 선택
```java
@PostConstruct
public void initializeFixedExtensions() {
    if (fixedExtensionRepository.count() > 0) return;  // 중복 방지
    
    Arrays.asList("bat", "cmd", "com", "cpl", "exe", "scr", "js")
          .forEach(this::createFixedExtension);
}
```

**장점**: 조건부 실행, Java 코드로 관리, 로그 출력 가능

### 7. 사용자 피드백
**고민**: 에러 상황을 사용자에게 어떻게 알릴 것인가?

**해결**: 구체적인 에러 메시지
```java
// 고정 확장자 중복
"확장자 'exe'는 고정 확장자에 이미 포함되어 있습니다."

// 커스텀 확장자 중복  
"확장자 'pdf'는 이미 등록되어 있습니다."

// 최대 개수 초과
"커스텀 확장자는 최대 200개까지만 등록할 수 있습니다."
```

### 8. API 응답 설계
**고민**: 토글 API에서 무엇을 반환할 것인가?

**해결**: 변경된 상태값 반환
```java
@PatchMapping("/fixed/{id}/toggle")
public ResponseEntity<ApiResponse<Boolean>> toggleFixedExtension(@PathVariable Long id) {
    boolean newStatus = fixedExtensionService.toggleBlocked(id);
    return ResponseEntity.ok(ApiResponse.of(newStatus, "상태가 변경되었습니다."));
}
```

**장점**: 클라이언트에서 별도 조회 없이 UI 즉시 업데이트 가능

## 📁 파일 구조
```
src/main/
├── java/
│   └── com/flow/domain/extension/
│       ├── controller/
│       │   ├── web/ExtensionWebController.java
│       │   └── api/ExtensionApiController.java
│       ├── service/
│       │   ├── FixedExtensionService.java
│       │   └── CustomExtensionService.java
│       ├── repository/
│       ├── entities/
│       └── dto/
└── resources/
    ├── templates/extensions.html
    └── static/
        ├── css/extensions.css
        └── js/extensions.js
```
