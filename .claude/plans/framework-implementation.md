# Framework境界 実装計画

## 決定事項

- **認証**: 共通機能として別途実装（本計画のスコープ外）
- **ページネーション**: 不要（MVP）
- **Command API**: 必要（ユーザー画面から規格を登録・更新）

---

## 現状サマリー

| 層 | 状況 | 備考 |
|---|---|---|
| ドメインモデル | ✅ 完了 | Framework, FrameworkVersion, RequirementCategory, Requirement, FrameworkControl, FrameworkControlPredecessor |
| リポジトリI/F | ✅ 完了 | FrameworkRepository |
| JPAエンティティ | ✅ 完了 | 全6エンティティ実装済み |
| JPAリポジトリ | ✅ 完了 | Spring Data JPA実装済み |
| アダプタ実装 | ✅ 完了 | FrameworkRepositoryImpl |
| DBスキーマ | ✅ 完了 | V1マイグレーション済み |
| **アプリケーション層** | ❌ 未実装 | |
| **プレゼンテーション層** | ❌ 未実装 | |

---

## 実装方針

### Phase 1: アプリケーション層（UseCase/Service）

**目的**: ドメインロジックをControllerから分離し、ユースケース単位で整理

#### 1.1 DTO作成
**ファイル**: `application/framework/dto/`

```
FrameworkDto.kt
FrameworkVersionDto.kt
RequirementCategoryDto.kt
RequirementDto.kt
FrameworkControlDto.kt
```

#### 1.2 Query Service（参照系）
**ファイル**: `application/framework/FrameworkQueryService.kt`

```kotlin
interface FrameworkQueryService {
    fun findAll(): List<FrameworkSummaryDto>
    fun findById(id: FrameworkId): FrameworkDetailDto?
    fun findVersionById(id: FrameworkVersionId): FrameworkVersionDetailDto?
    fun findControlsByVersionId(versionId: FrameworkVersionId): List<FrameworkControlDto>
}
```

#### 1.3 Command Service（更新系）
**ファイル**: `application/framework/FrameworkCommandService.kt`

```kotlin
interface FrameworkCommandService {
    fun createFramework(command: CreateFrameworkCommand): FrameworkId
    fun updateFramework(id: FrameworkId, command: UpdateFrameworkCommand)
    fun deleteFramework(id: FrameworkId)

    fun createVersion(command: CreateVersionCommand): FrameworkVersionId
    fun activateVersion(versionId: FrameworkVersionId)
    fun archiveVersion(versionId: FrameworkVersionId)

    fun createRequirementCategory(command: CreateCategoryCommand): RequirementCategoryId
    fun createRequirement(command: CreateRequirementCommand): RequirementId
    fun createFrameworkControl(command: CreateControlCommand): FrameworkControlId
}
```

#### 1.4 Command DTO
**ファイル**: `application/framework/command/`

```
CreateFrameworkCommand.kt
UpdateFrameworkCommand.kt
CreateVersionCommand.kt
CreateCategoryCommand.kt
CreateRequirementCommand.kt
CreateControlCommand.kt
```

---

### Phase 2: プレゼンテーション層（REST API）

**目的**: フロントエンドが利用するREST APIを提供

#### 2.1 Controller
**ファイル**: `presentation/framework/FrameworkController.kt`

```kotlin
@RestController
@RequestMapping("/api/frameworks")
class FrameworkController(
    private val queryService: FrameworkQueryService,
    private val commandService: FrameworkCommandService
) {
    // Query系
    @GetMapping
    fun listFrameworks(): List<FrameworkSummaryDto>

    @GetMapping("/{id}")
    fun getFramework(@PathVariable id: UUID): FrameworkDetailDto

    @GetMapping("/{id}/versions")
    fun listVersions(@PathVariable id: UUID): List<FrameworkVersionSummaryDto>

    @GetMapping("/versions/{versionId}")
    fun getVersion(@PathVariable versionId: UUID): FrameworkVersionDetailDto

    @GetMapping("/versions/{versionId}/controls")
    fun listControls(@PathVariable versionId: UUID): List<FrameworkControlDto>

    // Command系
    @PostMapping
    fun createFramework(@RequestBody request: CreateFrameworkRequest): ResponseEntity<FrameworkId>

    @PutMapping("/{id}")
    fun updateFramework(@PathVariable id: UUID, @RequestBody request: UpdateFrameworkRequest)

    @DeleteMapping("/{id}")
    fun deleteFramework(@PathVariable id: UUID)

    @PostMapping("/{id}/versions")
    fun createVersion(@PathVariable id: UUID, @RequestBody request: CreateVersionRequest): ResponseEntity<FrameworkVersionId>

    @PostMapping("/versions/{versionId}/activate")
    fun activateVersion(@PathVariable versionId: UUID)

    @PostMapping("/versions/{versionId}/archive")
    fun archiveVersion(@PathVariable versionId: UUID)
}
```

#### 2.2 エラーハンドリング
**ファイル**: `presentation/framework/FrameworkExceptionHandler.kt`

- `FrameworkNotFoundException` → 404
- その他のエラー → 500

---

### Phase 3: テスト

#### 3.1 単体テスト
- `FrameworkQueryServiceTest.kt` - サービス層のテスト（モック使用）

#### 3.2 統合テスト
- `FrameworkControllerIntegrationTest.kt` - API統合テスト（@SpringBootTest）

---

## API設計

### Query系（参照）

| Method | Endpoint | 説明 |
|--------|----------|------|
| GET | `/api/frameworks` | 全規格の一覧取得 |
| GET | `/api/frameworks/{id}` | 規格の詳細取得（versions含む） |
| GET | `/api/frameworks/{id}/versions` | 規格の版一覧取得 |
| GET | `/api/frameworks/versions/{versionId}` | 版の詳細取得（カテゴリ・要求・Control含む） |
| GET | `/api/frameworks/versions/{versionId}/controls` | 版配下の全FrameworkControl取得 |

### Command系（更新）

| Method | Endpoint | 説明 |
|--------|----------|------|
| POST | `/api/frameworks` | 規格を新規作成 |
| PUT | `/api/frameworks/{id}` | 規格を更新 |
| DELETE | `/api/frameworks/{id}` | 規格を削除 |
| POST | `/api/frameworks/{id}/versions` | 版を新規作成 |
| POST | `/api/frameworks/versions/{versionId}/activate` | 版をACTIVEに変更 |
| POST | `/api/frameworks/versions/{versionId}/archive` | 版をARCHIVEDに変更 |
| POST | `/api/frameworks/versions/{versionId}/categories` | カテゴリを追加 |
| POST | `/api/frameworks/categories/{categoryId}/requirements` | 要求を追加 |
| POST | `/api/frameworks/requirements/{requirementId}/controls` | FrameworkControlを追加 |

---

## 実装順序

### Step 1: DTO・Command定義
1. Query用DTO（`FrameworkSummaryDto`, `FrameworkDetailDto`等）
2. Command用DTO（`CreateFrameworkCommand`等）
3. Request/Response用DTO

### Step 2: アプリケーションサービス
1. `FrameworkQueryService` + `FrameworkQueryServiceImpl`
2. `FrameworkCommandService` + `FrameworkCommandServiceImpl`

### Step 3: プレゼンテーション層
1. `FrameworkController`
2. `FrameworkExceptionHandler`

### Step 4: テスト
1. サービス層の単体テスト
2. Controller層の統合テスト

---

## 作成するファイル一覧

```
src/main/kotlin/com/grc/platform/
├── application/
│   └── framework/
│       ├── dto/
│       │   ├── FrameworkSummaryDto.kt
│       │   ├── FrameworkDetailDto.kt
│       │   ├── FrameworkVersionSummaryDto.kt
│       │   ├── FrameworkVersionDetailDto.kt
│       │   ├── RequirementCategoryDto.kt
│       │   ├── RequirementDto.kt
│       │   └── FrameworkControlDto.kt
│       ├── command/
│       │   ├── CreateFrameworkCommand.kt
│       │   ├── UpdateFrameworkCommand.kt
│       │   ├── CreateVersionCommand.kt
│       │   ├── CreateCategoryCommand.kt
│       │   ├── CreateRequirementCommand.kt
│       │   └── CreateControlCommand.kt
│       ├── FrameworkQueryService.kt
│       ├── FrameworkQueryServiceImpl.kt
│       ├── FrameworkCommandService.kt
│       └── FrameworkCommandServiceImpl.kt
└── presentation/
    └── framework/
        ├── FrameworkController.kt
        ├── FrameworkExceptionHandler.kt
        └── request/
            ├── CreateFrameworkRequest.kt
            ├── UpdateFrameworkRequest.kt
            ├── CreateVersionRequest.kt
            ├── CreateCategoryRequest.kt
            ├── CreateRequirementRequest.kt
            └── CreateControlRequest.kt
```
