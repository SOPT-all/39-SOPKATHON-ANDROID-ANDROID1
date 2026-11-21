# 에이전트 작업 규칙

이 문서는 이 저장소에서 작업하는 Codex, Claude 등 모든 코딩 에이전트에 적용한다. 사용자의 현재 요청과 확인된 결정이 이 문서보다 우선한다.

## 브랜치

- `develop`: 완료된 개발 작업을 모으는 기준 브랜치. 새 작업 브랜치는 기본적으로 여기서 만든다.
- `feature`: 작은 단위의 기능 또는 설정 작업을 진행하는 작업 브랜치의 개념이다. 실제 브랜치명에는 아래 카테고리 접두사를 쓴다.
- `hotfix`: 긴급 오류 수정용 브랜치. 기준 브랜치는 요청에 명시된 브랜치를 따르고, 명시되지 않았다면 `develop`을 제안한다.
- `main`: 완성 버전 보관용이며 일상 작업의 기준 브랜치로 사용하지 않는다.

브랜치명은 `<카테고리>/#<이슈번호>-<작업명>` 형식을 사용한다. 작업명은 영문 소문자와 하이픈으로 간결하게 적는다.

| 카테고리 | 용도 |
| --- | --- |
| `feat` | 기능 구현 |
| `mod` | 기존 기능 수정 |
| `add` | 파일·설정·리소스 추가 |
| `del` | 삭제 |
| `fix` | 버그 수정 |
| `refactor` | 동작을 유지하는 구조 개선 |
| `init` | 프로젝트 초기 설정 |
| `hotfix` | 긴급 오류 수정 |

예: `feat/#25-search-api`, `feat/#30-home-ui`, `init/#1-add-font`.

**새 브랜치를 만들기 전에는 항상 사용자에게 정확한 브랜치명과 기준 브랜치를 제안하고 확인받는다.** 이름이 요청에 이미 적혀 있어도 생성 전에 확인한다. 확인 후 기존 브랜치 존재 여부와 작업 트리 상태를 확인하고 생성한다. 기존 변경 사항을 임의로 덮어쓰지 않는다.

## 커밋과 PR

- 커밋 제목: `[카테고리/#이슈번호] 작업 내용`
- 예: `[feat/#30] 홈 화면 구현`, `[add/#1] 폰트 파일 추가`
- PR 제목: `[유형/#이슈번호] 작업 내용` (예: `[FEAT/#6] 홈화면 구현`)
- PR 본문의 `Related issue`에 `closed #이슈번호`를 적는다. 현재 GitHub 기본 브랜치는 `develop`이며, `develop` 대상 PR이 병합되면 이 키워드로 이슈가 자동 종료된다.
- 이슈는 `.github/ISSUE_TEMPLATE/issue.md`, PR은 `.github/pull_request_template.md`를 사용한다.

## Android 구현 기준

- 의존성 주입: Hilt.
- 아키텍처: Google 권장 앱 아키텍처를 따르는 MVVM. UI는 화면과 ViewModel, 데이터 계층은 저장소와 데이터 소스로 구성한다. 도메인 계층은 필요할 때만 추가한다.
- UI 상태는 ViewModel에서 관리하고 화면에는 상태와 이벤트를 전달한다.
- 공통 UI 테마·컴포넌트는 `core/designsystem`, 화면 이동은 `core/navigation`, 기능 화면은 `feature`, API DTO는 `data/dto`에 둔다.
- API 응답 모델은 `data/dto/base/BaseResponse.kt`의 `status`, `code`, `message`, nullable `data` 형식을 기준으로 한다. 화면 분기는 `message`가 아닌 `code`를 사용한다. 실제 서버 주소나 개별 응답 필드는 API 명세가 확인되기 전까지 추측해서 추가하지 않는다.
- 의존성 버전은 `gradle/libs.versions.toml`에서 관리한다.

## 화면 연결과 데이터 흐름

ViewModel의 상태·이벤트 연결이 필요한 기능 화면은 경로 객체 `<화면명>Destination`, 연결 Composable `<화면명>Route`, UI Composable `<화면명>Screen`으로 역할을 구분한다. 모든 화면에 세 구성을 강제하지 않는다. 단순 진입 화면이나 정적 화면은 별도 Route Composable 없이 Screen을 등록해도 된다. 내비게이션 대상인 경우에는 경로 식별자가 필요하다.

현재 은지 화면에 Mock 조회용 ViewModel과 Repository를 연결한다. Main은 화면 선택 안내만 표시하는 단순 진입 화면이며, 별도 Route Composable이나 ViewModel을 두지 않는다. 혜민·희진·승연 기본 화면의 경로 객체는 아직 기존 `*Route` 이름을 사용한다.

| 구성 | 역할 | 은지 화면 예시 |
| --- | --- | --- |
| Destination | 직렬화 가능한 내비게이션 경로 식별자 | `EunjiDestination` |
| NavHost | 경로 등록, Route 호출, 화면 이동 콜백 연결 | `AppNavHost` |
| Route | Hilt로 ViewModel을 얻고 상태를 구독해 Screen에 상태와 이벤트 전달 | `EunjiRoute` |
| Screen | 전달받은 상태로 UI를 표시하고 사용자 이벤트 전달 | `EunjiScreen` |
| ViewModel | Repository를 호출하고 UI 상태 및 조회 작업 관리 | `EunjiViewModel` |
| Repository | UI 계층이 사용할 데이터 조회 계약과 구현 | `EunjiRepository`, `FakeEunjiRepository` |
| DataSource | Mock 또는 원격 데이터 조회 | `FakeEunjiDataSource` |

현재 앱의 진입·조회·화면 갱신 흐름은 다음과 같다.

```text
MainActivity
  → AppNavHost
    → 시작: composable<MainDestination> { MainScreen() } (화면 선택 안내)
    → 은지 탭 선택: navigateToTab(EunjiDestination)
      → composable<EunjiDestination> { EunjiRoute() }
        → EunjiRoute: Hilt ViewModel 획득, collectAsStateWithLifecycle() 구독
          → EunjiScreen(uiState, onRetry): UI 표시

EunjiViewModel: 진입 시 loadItems()
  → EunjiRepository.getItems()
    → Hilt가 주입한 FakeEunjiRepository
      → FakeEunjiDataSource: 임시 목록 반환
  → EunjiUiState 갱신: Loading → Success / Empty / Error
    → StateFlow 구독을 통해 EunjiScreen 갱신

EunjiScreen에서 다시 시도
  → EunjiRoute가 전달한 onRetry
    → EunjiViewModel.loadItems()
```

Screen에서 ViewModel이나 NavController를 직접 얻지 않는다. 화면 이동이 필요하면 NavHost에서 Route로 이동 콜백을 전달하고, Route가 Screen 이벤트와 연결한다. 현재 탭 선택과 백스택 처리는 `AppNavHost`와 `navigateToTab()`이 담당한다. 별도 `MainAppState`는 아직 도입하지 않았다.

### Mock 데이터 사용

앱 실행 후 하단의 은지 탭을 선택한다. Main에서는 Mock 데이터를 조회하지 않는다.

- `data/model/EunjiItem.kt`는 서버 DTO와 분리된 임시 화면용 모델이다. 실제 기능 요구사항이 정해지면 해당 모델과 조회 계약을 조정한다.
- `data/di/RepositoryModule.kt`에서 `EunjiRepository`에 `FakeEunjiRepository`를 바인딩한다. ViewModel은 Fake 구현체를 직접 참조하지 않는다.
- 같은 파일의 `FakeDataSourceModule.provideEunjiDataSource()`에서 `FakeEunjiScenario.SUCCESS`, `EMPTY`, `ERROR` 중 하나를 설정하고 다시 실행해 목록·빈 목록·오류 화면을 확인한다.
- 기본 지연은 500ms이며, 이 동안 로딩을 표시한다. `ERROR` 시나리오는 재시도해도 실패한다.
- `EunjiScreen.kt`의 Compose Preview로 네 가지 상태의 UI를 확인할 수 있다.

### 서버 데이터로 교체

1. API 명세 확정 후 요청·응답 DTO와 Retrofit API, 원격 데이터 소스를 구현한다.
2. `EunjiRepository`의 서버 구현체(예: `RemoteEunjiRepository`)를 추가한다. DTO를 화면용 모델로 변환하고, 빈 목록·실패·코루틴 취소에 대한 계약을 유지한다. 실패는 예외로 전달하고 코루틴 취소를 삼키지 않는다.
3. 원격 API와 데이터 소스를 Hilt에 등록한 후 `RepositoryModule.bindEunjiRepository()`의 구현체를 `FakeEunjiRepository`에서 서버 구현체로 교체한다. 사용하지 않는 Fake 데이터 소스 제공 모듈을 제거한다.
4. Repository의 함수와 반환 모델이 유지되면 Route·Screen·ViewModel은 그대로 사용한다. API 명세에 따라 계약이나 화면 요구사항이 바뀌면 해당 부분도 조정한다.

현재 Fake 구현은 모든 빌드에서 사용하므로 실제 배포 전에 서버 구현체로 교체한다. 서버 실패를 Mock 성공 데이터로 자동 대체하지 않는다.

작업 후 관련 빌드를 확인한다. 기본 검증 명령은 `./gradlew :app:assembleDebug`이다.
Mock 상태 전환·재시도·취소를 포함한 단위 테스트는 `./gradlew :app:testDebugUnitTest`로 확인한다.
