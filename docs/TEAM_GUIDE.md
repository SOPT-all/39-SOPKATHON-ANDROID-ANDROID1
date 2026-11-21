# Android 팀 작업 가이드

이 문서는 브랜치, 커밋, PR 규칙과 현재 Android 프로젝트의 기본 구조를 정리합니다.

## 브랜치

`develop`에 완료된 개발 작업을 모읍니다. 작은 단위의 작업은 `develop`에서 별도 브랜치를 만들어 진행합니다. `main`은 완성 버전 보관용이며 일반 작업의 시작점으로 사용하지 않습니다. 긴급 수정은 `hotfix` 브랜치로 구분합니다.

브랜치명은 `<카테고리>/#<이슈번호>-<작업명>` 형식으로, 작업명은 영문 소문자와 하이픈을 사용합니다.

| 카테고리 | 용도 | 예시 |
| --- | --- | --- |
| `feat` | 기능 구현 | `feat/#25-search-api` |
| `mod` | 기존 기능 수정 | `mod/#12-home-layout` |
| `add` | 파일·설정·리소스 추가 | `add/#1-font` |
| `del` | 삭제 | `del/#14-unused-screen` |
| `fix` | 버그 수정 | `fix/#18-login-error` |
| `refactor` | 동작을 유지하는 구조 개선 | `refactor/#20-data-layer` |
| `init` | 프로젝트 초기 설정 | `init/#1-add-font` |
| `hotfix` | 긴급 오류 수정 | `hotfix/#21-crash` |

코딩 에이전트가 새 브랜치를 만들 때는 정확한 브랜치명과 기준 브랜치를 먼저 제안하고 요청자에게 확인받습니다. 이 규칙은 [에이전트 작업 규칙](../AGENTS.md)에도 적혀 있습니다.

## 커밋과 PR

- 커밋 제목: `[카테고리/#이슈번호] 작업 내용` (예: `[feat/#30] 홈 화면 구현`, `[add/#1] 폰트 파일 추가`)
- PR 제목: `[유형/#이슈번호] 작업 내용` (예: `[FEAT/#6] 홈화면 구현`)
- 이슈 작성 시 [이슈 템플릿](../.github/ISSUE_TEMPLATE/issue.md), PR 작성 시 [PR 템플릿](../.github/pull_request_template.md)을 사용합니다.
- PR의 `Related issue`에 실제 번호를 넣어 `closed #6`처럼 적습니다.
- 이 저장소에서 생성한 PR이 열리거나 리뷰 준비 상태가 되면 [리뷰 요청 워크플로](../.github/workflows/request-android-team-review.yml)가 `SOPT-all/android39_one` 팀에 리뷰를 요청합니다.

GitHub 기본 브랜치가 `main`인 동안 이슈 자동 종료 키워드는 `main` 대상 PR에서만 작동합니다. `develop` 대상 PR에서는 관련 이슈를 별도로 연결하거나 종료해야 합니다.

## Android 구조

- 의존성 주입은 **Hilt**, 화면 구조는 **MVVM**을 사용합니다.
- Google 권장 앱 아키텍처에 따라 UI 계층은 화면과 ViewModel, 데이터 계층은 저장소와 데이터 소스로 구성합니다. 도메인 계층은 필요한 경우에만 추가합니다.
- 공통 테마와 컴포넌트는 `core/designsystem`, 화면 이동은 `core/navigation`, 기능별 화면은 `feature`, API DTO는 `data/dto`에 둡니다.
- 공통 API 응답 모델은 [`BaseResponse`](../app/src/main/java/com/android/sopkathon_android1/data/dto/base/BaseResponse.kt)이며 `status: Int`, `code: String`, `message: String`, `data: T?` 필드를 사용합니다. 화면 분기는 `message`가 아닌 `code`를 기준으로 합니다.
- 서버 기본 주소는 [`NetworkModule`](../app/src/main/java/com/android/sopkathon_android1/core/network/NetworkModule.kt)에 설정되어 있습니다. 서버와 개별 API가 준비되면 Retrofit 인터페이스에 상대 경로와 요청·응답 DTO를 추가합니다.
- 의존성 버전은 [`gradle/libs.versions.toml`](../gradle/libs.versions.toml)에서 관리합니다.

## 확인 방법

변경 후 프로젝트 루트에서 다음 명령으로 Android 디버그 빌드를 확인합니다.

```bash
./gradlew :app:assembleDebug
```
