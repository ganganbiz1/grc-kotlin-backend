---
paths: src/**/*.kt
---

# Kotlinコーディング規約

## DDD（ドメイン駆動設計）

### Entity

- **通常の `class` を使用する**（`data class` は使用しない）
- `equals()` / `hashCode()` は**IDのみ**で判定する
- `toString()` はデバッグ用に実装する

```kotlin
// Good
class User(
    val id: UserId,
    val name: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id
    }

    override fun hashCode(): Int = id.hashCode()

    override fun toString(): String = "User(id=$id, name=$name)"
}

// Bad - EntityにはNG
data class User(val id: UserId, val name: String)
```

### Value Object

- **`data class`** または **`@JvmInline value class`** を使用する
- 不変（immutable）であること

```kotlin
// 複数プロパティの場合
data class Money(val amount: BigDecimal, val currency: Currency)

// 単一プロパティの場合（推奨）
@JvmInline
value class UserId(val value: String)
```

### ID

- **`@JvmInline value class`** を使用する
- 型安全性とパフォーマンスを両立
- `generate()` メソッドでUUID v7を生成

```kotlin
@JvmInline
value class UserId(val value: String) {
    companion object {
        fun generate(): UserId = UserId(UUIDv7.generate().toString())
    }
}
```

## UUID

- **UUID v7**（タイムスタンプベース、ソート可能）を使用する
- ライブラリ: `java-uuid-generator`

```kotlin
import com.grc.platform.domain.shared.UUIDv7

val id = UUIDv7.generate()  // UUID v7を生成
```

## ファイル構成

- **エンティティ + ID 同居**: 各ファイルにEntityとそのID Value Objectを同居させる

```
model/
├── User.kt           # User + UserId
├── Order.kt          # Order + OrderId
└── Product.kt        # Product + ProductId
```

## パッケージ構成（レイヤードアーキテクチャ）

ドメイン層はORMやDBに一切依存しない。JPA EntityはInfrastructure層に隔離する。

```
com.grc.platform/
├── domain/                        # ドメイン層（純粋なビジネスロジック）
│   ├── {context}/
│   │   ├── model/                 # ドメインモデル（ORMに依存しない）
│   │   │   ├── Control.kt         # Entity + ID
│   │   │   └── Framework.kt
│   │   └── repository/            # Repository Interface（抽象）
│   │       └── FrameworkRepository.kt
│   └── shared/                    # 共通ユーティリティ
│       └── UUIDv7.kt
│
├── infrastructure/                # インフラ層（技術的な実装）
│   └── persistence/               # 永続化関連
│       └── {context}/
│           ├── entity/            # JPA Entity（データモデル）
│           │   └── ControlJpaEntity.kt
│           ├── repository/        # Spring Data JPA Repository
│           │   └── ControlJpaRepository.kt
│           └── adapter/           # Domain Repository の実装
│               └── ControlRepositoryImpl.kt
│
├── application/                   # アプリケーション層（ユースケース）
│   └── {context}/
│       └── FrameworkService.kt
│
└── presentation/                  # プレゼンテーション層（API）
    └── {context}/
        └── FrameworkController.kt
```

### 各層の責務

| 層 | 責務 | 依存可能な層 |
|---|---|---|
| domain | ビジネスロジック、ドメインモデル | なし（純粋） |
| infrastructure | DB永続化、外部API連携 | domain |
| application | ユースケース、トランザクション制御 | domain, infrastructure |
| presentation | HTTP API、リクエスト/レスポンス変換 | application |

### 命名規則（Infrastructure層）

| 種類 | 命名 | 例 |
|---|---|---|
| JPA Entity | `{Entity}JpaEntity` | `ControlJpaEntity` |
| JPA Repository | `{Entity}JpaRepository` | `ControlJpaRepository` |
| Repository実装 | `{Entity}RepositoryImpl` | `ControlRepositoryImpl` |

## 命名規則

| 種類 | 命名 | 例 |
|------|------|-----|
| Entity | 名詞（単数形） | `User`, `Order` |
| Value Object | 名詞 | `Money`, `Address` |
| ID | `{Entity}Id` | `UserId`, `OrderId` |
| Repository | `{Entity}Repository` | `UserRepository` |
| Service | `{UseCase}Service` | `CreateOrderService` |

## Null安全

- `?` を使う場合は意図を明確にする
- `!!` は原則使用禁止（例外: テストコード）

```kotlin
// Good
val name: String? = user?.name

// Bad
val name: String = user!!.name
```

## データベース設計

### 基本方針

- **DBはシンプルに保つ**: ロジックはアプリケーション側で担保する
- **AIが仕様を確認しやすくする**: DBまで確認しなくてもアプリコードで完結

### DEFAULTは使用しない

- `created_at`, `updated_at`: アプリ側でUTCを明示的にセット
- `display_order`: アプリ側で明示的にセット
- **理由**: タイムゾーン問題の回避、テスト時の柔軟性、ロジックの集約

```sql
-- Good
created_at TIMESTAMP NOT NULL,
updated_at TIMESTAMP NOT NULL

-- Bad
created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
```

### ON DELETE CASCADEは使用しない

- 削除ロジックはアプリケーション側で制御する
- **理由**: 意図しない連鎖削除の防止、削除前のバリデーション実装が容易

### マッピングテーブル

- N:Nの関連テーブルにも**IDを付与する**（単なる関連ではなくEntityとして扱う）
- **理由**: GRCドメインではマッピング自体が監査対象になり得る

```sql
-- Good: マッピング自体がEntity
CREATE TABLE requirement_controls (
    id VARCHAR(36) PRIMARY KEY,
    requirement_id VARCHAR(36) NOT NULL REFERENCES requirements(id),
    control_id VARCHAR(36) NOT NULL REFERENCES controls(id),
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL,
    UNIQUE (requirement_id, control_id)
);

-- Bad: 単なる関連テーブル
CREATE TABLE requirement_controls (
    requirement_id VARCHAR(36) NOT NULL,
    control_id VARCHAR(36) NOT NULL,
    PRIMARY KEY (requirement_id, control_id)
);
```

### ドメインモデルとデータモデルの分離

- **ドメインモデル（domain層）**: ビジネスロジックに必要なプロパティのみ
- **データモデル（infrastructure層）**: DBマッピング用、監査カラム含む

```kotlin
// ドメインモデル - createdAt/updatedAt は含めない
class Requirement(
    val id: RequirementId,
    val name: String,
    val displayOrder: Int,  // ビジネスロジックなので含める
    val controls: List<Control>
)

// データモデル（JPA Entity） - 監査カラムを含む
@Entity
class RequirementEntity(
    @Id val id: String,
    val name: String,
    val displayOrder: Int,
    val createdAt: Instant,
    val updatedAt: Instant
)
```

### N:N関連のドメインモデル表現

- マッピングに追加属性がない場合: `List<関連Entity>` で表現
- マッピングに追加属性がある場合: 独立したドメインモデルを作成

```kotlin
// 現状: マッピングに属性がないので List<Control> で十分
class Requirement(
    val controls: List<Control>
)

// 将来: マッピングに属性が必要になったら独立Entity化
class RequirementControl(
    val id: RequirementControlId,
    val requirementId: RequirementId,
    val controlId: ControlId,
    val rationale: String,      // なぜ紐付けたか
    val approvedBy: UserId      // 誰が承認したか
)
```

### 表示順序（display_order）

- 親子関係がある場合、子テーブルに `display_order` カラムを追加
- 親IDと `display_order` の組み合わせでUNIQUE制約

```sql
display_order INT NOT NULL,
-- ...
UNIQUE (parent_id, display_order)
```

### コメント

- テーブル・カラムにコメントを必須とする

```sql
COMMENT ON TABLE requirements IS '要件。フレームワークの最小単位となる具体的な要求事項';
COMMENT ON COLUMN requirements.id IS '主キー（UUID v7）';
```
